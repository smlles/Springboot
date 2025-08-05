package com.edutalk.edutalk.util;

import com.edutalk.edutalk.model.ContractorEntity;
import com.edutalk.edutalk.repository.ContractorRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenProvider {

    @Autowired
    private ContractorRepository contractorRepository;

    @Value("${jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    public String generateToken(String identy, String type, String sub, String id, String name, Date expirationDate) {
        Optional<ContractorEntity> contractorOptional = contractorRepository.findByIdenty(identy);
        if (contractorOptional.isEmpty()) {
            throw new RuntimeException("Contractor not found for identy: " + identy);
        }
        ContractorEntity contractor = contractorOptional.get();

//        if (contractor.getPrivateKey() == null) {
//            throw new RuntimeException("Private key not found for contractor: " + identy);
//        }

        try {
            // PEM 형식의 private key 문자열에서 헤더/푸터 및 공백 제거
//            byte[] privateKeyBytes = Base64.getDecoder().decode(
//                    contractor.getPrivateKey()
//                            .replace("-----BEGIN PRIVATE KEY-----", "")
//                            .replace("-----END PRIVATE KEY-----", "")
//                            .replaceAll("\\s", "")
//            );
//            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            // ClaimsBuilder를 사용하여 Claims 객체 생성
            Claims claims = Jwts.claims()
                    .setSubject(id) // 'id'를 JWT의 'sub' (Subject)로 설정
                    .add("identy", identy) // add() 메서드를 사용하여 커스텀 클레임 추가
                    .add("type", type)
                    .add("name", name)
                    .build(); // build()를 호출하여 Claims 객체 완성

            Date now = new Date();
            // 만료 날짜가 제공되면 사용하고, 그렇지 않으면 기본 만료 시간 사용
            Date expiryDateToUse = (expirationDate != null) ? expirationDate : new Date(now.getTime() +
                    accessTokenExpirationMs);

            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(expiryDateToUse)
//                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT token: " + e.getMessage(), e);
        }
    }

    public Claims getClaimsFromToken(String token, String identy) {
        Optional<ContractorEntity> contractorOptional = contractorRepository.findByIdenty(identy);
        if (contractorOptional.isEmpty()) {
            throw new RuntimeException("Contractor not found for identy: " + identy);
        }
        ContractorEntity contractor = contractorOptional.get();

        if (contractor.getPublicKey() == null) {
            throw new RuntimeException("Public key not found for contractor: " + identy);
        }

        try {
            // PEM 형식의 public key 문자열에서 헤더/푸터 및 공백 제거
            byte[] publicKeyBytes = Base64.getDecoder().decode(
                    contractor.getPublicKey()
                            .replace("-----BEGIN PUBLIC KEY-----", "")
                            .replace("-----END PUBLIC KEY-----", "")
                            .replaceAll("\\s", "")
            );
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            return Jwts.parser() 
            		   .verifyWith(publicKey)
            		   .build()
            		   .parseSignedClaims(token)
            		   .getPayload();
             
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token: " + e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token: " + e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            throw e;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error validating JWT token: " + e.getMessage(), e);
        }
    }

    public boolean validateToken(String authToken, String identy) {
        try {
            getClaimsFromToken(authToken, identy);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token, String identy) {
        return getClaimsFromToken(token, identy).getSubject();
    }

    public String getIdentyFromToken(String token, String identy) {
        return getClaimsFromToken(token, identy).get("identy", String.class);
    }
}
