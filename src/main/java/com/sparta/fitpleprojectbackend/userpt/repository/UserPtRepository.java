package com.sparta.fitpleprojectbackend.userpt.repository;

import com.sparta.fitpleprojectbackend.userpt.entity.UserPt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPtRepository extends JpaRepository<UserPt, Long> {
}