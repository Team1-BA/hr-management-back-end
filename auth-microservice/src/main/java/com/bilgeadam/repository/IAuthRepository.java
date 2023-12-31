package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAuthRepository extends JpaRepository<Auth,Long> {

    Optional<Auth> findOptionalByCompanyEmailAndPassword(String email, String password);
    Optional<Auth> findOptionalByCompanyEmail(String companyEmail);

    Optional<Auth> findOptionalById(Long authid);

    Optional<Auth> findByUsername(String username);

    Optional<Auth> findOptionalByPersonalEmail(String email);
}
