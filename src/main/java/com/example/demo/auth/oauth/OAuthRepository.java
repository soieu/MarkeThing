package com.example.demo.auth.oauth;

import com.example.demo.siteuser.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthRepository extends JpaRepository<SiteUser, Long> {

    SiteUser findByEmail(String email);


}
