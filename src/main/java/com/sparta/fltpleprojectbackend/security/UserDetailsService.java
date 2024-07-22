package com.sparta.fltpleprojectbackend.security;

import com.sparta.fltpleprojectbackend.user.entity.User;
import com.sparta.fltpleprojectbackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  /**
   * 유저 이름을 통해 사용자 정보 로드
   * @param username 유저 이름
   * @return UserDetails 사용자 정보
   * @throws UsernameNotFoundException 유저가 없을 때 발생
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOptional = userRepository.findByUsername(username);
    User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    return new UserDetailsImpl(user);
  }
}