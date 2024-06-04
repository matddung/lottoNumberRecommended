package com.studyjun.lotto.oAuth2.entity;

import java.util.Map;

public class KakaoOAuth2MemberInfo extends OAuth2MemberInfo{
    public KakaoOAuth2MemberInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getNickname() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");

        if (account == null || profile == null) return null;

        return (String) profile.get("nickname");
    }
}
