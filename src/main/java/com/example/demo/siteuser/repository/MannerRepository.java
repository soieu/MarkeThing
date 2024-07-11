package com.example.demo.siteuser.repository;

import com.example.demo.siteuser.entity.Manner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface MannerRepository extends JpaRepository<Manner, Long> {

}
