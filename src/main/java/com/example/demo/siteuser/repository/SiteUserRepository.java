package com.example.demo.siteuser.repository;

import com.example.demo.siteuser.entity.SiteUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {

    Optional<SiteUser> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<SiteUser> findByPhoneNumber(String phoneNumber);

    Optional<SiteUser> findByEmailAndPhoneNumber(String email, String phoneNumber);

    Optional<SiteUser> findById(Long id);

}