// src/main/java/com/sparta/fitpleprojectbackend/oauth/CustomOauth2UserDetails.java
package com.sparta.fitpleprojectbackend.oauth;

import com.sparta.fitpleprojectbackend.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOauth2UserDetails extends DefaultOAuth2User {

    private final User user;

    public CustomOauth2UserDetails(User user, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        super(authorities, attributes, "sub");
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}