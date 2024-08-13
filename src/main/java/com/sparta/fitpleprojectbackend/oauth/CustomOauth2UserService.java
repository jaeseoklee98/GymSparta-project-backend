// src/main/java/com/sparta/fitpleprojectbackend/oauth/CustomOauth2UserService.java
package com.sparta.fitpleprojectbackend.oauth;

import com.sparta.fitpleprojectbackend.enums.Role;
import com.sparta.fitpleprojectbackend.user.entity.Oauth2User;
import com.sparta.fitpleprojectbackend.user.entity.User;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // src/main/java/com/sparta/fitpleprojectbackend/oauth/CustomOauth2UserService.java

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());

        // if(provider.equals("google"))의 밑에 아래 코드 추가
//        if(provider.equals("google")){
//            log.info("구글 로그인");
//            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
//
//        } else if (provider.equals("kakao")) {
//            log.info("카카오 로그인");
//            oAuth2UserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
//        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String accountId = email; // Set accountId to email
        String name = oAuth2UserInfo.getName();

        Optional<User> findUserOptional = userRepository.findByAccountId(accountId);
        User user;

        if (findUserOptional.isEmpty()) {
            user = new User();
            user.setAccountId(accountId);
            user.setEmail(email); // Set email to the same value as accountId
            user.setUserName(name);
            user.setPassword("default_password"); // Set a default password
            user.setRole(Role.USER);
            userRepository.save(user);

            Oauth2User oauth2User = new Oauth2User();
            oauth2User.setProvider(provider);
            oauth2User.setProviderId(providerId);
            oauth2User.setUser(user);
            user.setOauth2User(oauth2User);
            userRepository.save(user);
        } else {
            user = findUserOptional.get();
        }

        return new CustomOauth2UserDetails(user, Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())), oAuth2User.getAttributes());
    }
}