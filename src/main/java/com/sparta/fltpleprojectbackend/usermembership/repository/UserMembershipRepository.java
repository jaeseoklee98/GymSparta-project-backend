package com.sparta.fltpleprojectbackend.usermembership.repository;

import com.sparta.fltpleprojectbackend.usermembership.entity.UserMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMembershipRepository extends JpaRepository<UserMembership, Long> {
    List<UserMembership> findByUserId(Long userId);
}
