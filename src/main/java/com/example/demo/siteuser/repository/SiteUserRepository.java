package com.example.demo.siteuser.repository;

import com.example.demo.siteuser.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {

    SiteUser findByEmail(String email);

    boolean existsByEmail(String email);
}
