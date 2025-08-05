 package com.edutalk.edutalk.repository;


  import com.edutalk.edutalk.model.ContractorEntity;
  import org.springframework.data.jpa.repository.JpaRepository;
  import org.springframework.stereotype.Repository;

  import java.util.Optional;
  import java.util.UUID;


  @Repository
  public interface ContractorRepository extends JpaRepository<ContractorEntity, UUID> {

      /*
        * 이메일 주소를 기준으로 사용자를 조회합니다.
        * @param email 사용자 이메일
        * @return Optional<ContractorEntity>
       */
      Optional<ContractorEntity> findByEmail(String email);
      Optional<ContractorEntity> findByIdenty(String identy);
  }