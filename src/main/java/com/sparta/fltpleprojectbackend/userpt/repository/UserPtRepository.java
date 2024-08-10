package com.sparta.fltpleprojectbackend.userpt.repository;

import com.sparta.fltpleprojectbackend.userpt.entity.UserPt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPtRepository extends JpaRepository<UserPt, Long> {
}