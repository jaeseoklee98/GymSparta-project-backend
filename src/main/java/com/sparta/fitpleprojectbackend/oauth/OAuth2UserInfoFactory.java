package com.sparta.fitpleprojectbackend.oauth;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase("google")) {
            return new GoogleUserDetails(attributes);
        } else if (registrationId.equalsIgnoreCase("kakao")) {
            return new KakaoUserDetails(attributes);
        } else if(registrationId.equalsIgnoreCase("naver")) {
            return new NaverUserDetails(attributes);
        }{
            throw new IllegalArgumentException("Unsupported provider: " + registrationId);
        }
    }
}