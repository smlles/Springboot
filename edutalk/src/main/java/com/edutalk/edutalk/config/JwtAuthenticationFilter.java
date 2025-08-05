package com.edutalk.edutalk.config;

import com.edutalk.edutalk.util.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                String identy = null;
                try {
                    String[] parts = jwt.split("\\.");
                    if (parts.length == 3) {
                        String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
                        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                        java.util.Map<String, Object> payloadMap = mapper.readValue(payload, java.util.Map.class);
                        identy = (String) payloadMap.get("identy");
                    }
                } catch (Exception e) {
                    logger.warn("Could not get identy from JWT token payload: " + e.getMessage());
                    filterChain.doFilter(request, response);
                    return;
                }

                if (identy != null) {
                    Claims claims = tokenProvider.getClaimsFromToken(jwt, identy);
                    String userId = claims.get("id", String.class); // 'id' 클레임에서 사용자 ID 추출

                    if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token", ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired JWT token");
            return;
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token", ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unsupported JWT token");
            return;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token", ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature", ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT signature");
            return;
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty", ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT claims string is empty");
            return;
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication error");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        String queryToken = request.getParameter("token");
        if (StringUtils.hasText(queryToken)) {
            return queryToken;
        }
        return null;
    }
}
