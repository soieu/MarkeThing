package com.example.demo.community.repository;

import com.example.demo.common.filter.repository.CommunityFilteringRepository;
import com.example.demo.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepositoryCommunity extends JpaRepository<Community, Long>,
        CommunityFilteringRepository {

}
