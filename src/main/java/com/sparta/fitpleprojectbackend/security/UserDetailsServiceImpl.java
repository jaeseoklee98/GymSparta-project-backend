package com.sparta.fitpleprojectbackend.security;

import com.sparta.fitpleprojectbackend.owner.entity.Owner;
import com.sparta.fitpleprojectbackend.owner.repository.OwnerRepository;
import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
import com.sparta.fitpleprojectbackend.trainer.repository.TrainerRepository;
import com.sparta.fitpleprojectbackend.user.entity.User;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements
    org.springframework.security.core.userdetails.UserDetailsService {

  private final UserRepository userRepository;
  private final OwnerRepository ownerRepository;
  private final TrainerRepository trainerRepository;

  public UserDetailsServiceImpl(UserRepository userRepository, OwnerRepository ownerRepository,
      TrainerRepository trainerRepository) {
    this.userRepository = userRepository;
    this.ownerRepository = ownerRepository;
    this.trainerRepository = trainerRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
    Optional<User> userOptional = userRepository.findByAccountId(accountId);
    if (userOptional.isPresent()) {
      return new UserDetailsImpl(userOptional.get());
    }

    Optional<Owner> ownerOptional = ownerRepository.findByAccountId(accountId);
    if (ownerOptional.isPresent()) {
      return new UserDetailsImpl(ownerOptional.get());
    }

    Optional<Trainer> trainerOptional = trainerRepository.findByAccountId(accountId);
    if (trainerOptional.isPresent()) {
      return new UserDetailsImpl(trainerOptional.get());
    }

    throw new UsernameNotFoundException(
        "사용자, 점주 또는 트레이너를 찾을 수 없습니다. 아이디: " + accountId);
  }
}