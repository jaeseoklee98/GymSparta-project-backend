package com.sparta.fitpleprojectbackend.owner.repository;

import com.sparta.fitpleprojectbackend.owner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

  Optional<Owner> findByAccountId(String accountId);

  Optional<Owner> findByEmail(String email);

  Optional<Owner> findByOwnerPhoneNumber(String ownerPhoneNumber);

  Optional<Owner> findByForeignerRegistrationNumber(String foreignerRegistrationNumber);

  Optional<Owner> findByResidentRegistrationNumber(String residentRegistrationNumber);

  Optional<Owner> findByAccountIdAndOwnerStatus(String accountId, String status);

  Optional<Owner> findByEmailAndOwnerStatus(String email, String status);

  Optional<Owner> findByOwnerPhoneNumberAndOwnerStatus(String ownerPhoneNumber, String status);

  void deleteAllByScheduledDeletionDateBefore(LocalDateTime now);
}