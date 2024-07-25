package com.sparta.fltpleprojectbackend.security;

import com.sparta.fltpleprojectbackend.owner.entity.Owner;
import com.sparta.fltpleprojectbackend.owner.repository.OwnerRepository;
import com.sparta.fltpleprojectbackend.user.entity.User;
import com.sparta.fltpleprojectbackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private OwnerRepository ownerRepository;

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

    throw new UsernameNotFoundException("User or Owner not found with accountId: " + accountId);
  }
}