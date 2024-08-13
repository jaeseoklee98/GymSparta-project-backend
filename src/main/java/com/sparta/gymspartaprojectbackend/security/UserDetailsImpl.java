package com.sparta.gymspartaprojectbackend.security;

import com.sparta.gymspartaprojectbackend.enums.Role;
import com.sparta.gymspartaprojectbackend.owner.entity.Owner;
import com.sparta.gymspartaprojectbackend.trainer.entity.Trainer;
import com.sparta.gymspartaprojectbackend.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class UserDetailsImpl implements UserDetails {

  private final String accountId;

  private final String password;

  private final Role role;

  private final User user;

  private final Owner owner;

  private final Trainer trainer;

  public UserDetailsImpl(User user) {
    this.accountId = user.getAccountId();
    this.password = user.getPassword();
    this.role = user.getRole();
    this.user = user;
    this.owner = null;
    this.trainer = null;
  }

  public UserDetailsImpl(Owner owner) {
    this.accountId = owner.getAccountId();
    this.password = owner.getPassword();
    this.role = owner.getRole();
    this.user = null;
    this.owner = owner;
    this.trainer = null;
  }

  public UserDetailsImpl(Trainer trainer) {
    this.accountId = trainer.getAccountId();
    this.password = trainer.getPassword();
    this.role = trainer.getRole();
    this.user = null;
    this.owner = null;
    this.trainer = trainer;
  }

  /**
   * 사용자 권한
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {

    return password;
  }

  @Override
  public String getUsername() {

    return accountId;
  }

  @Override
  public boolean isAccountNonExpired() {

    return true;
  }

  @Override
  public boolean isAccountNonLocked() {

    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {

    return true;
  }

  @Override
  public boolean isEnabled() {

    return true;
  }

  public Long getUserId() {
    if (user != null) {
      return user.getId();
    } else if (owner != null) {
      return owner.getId();
    } else if (trainer != null) {
      return trainer.getId();
    } else {
      throw new IllegalStateException("존재하지 않는 사용자 입니다.");
    }
  }
}