package com.example.demo.community.repository;

import com.example.demo.community.entity.Community;
import com.example.demo.siteuser.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

}
