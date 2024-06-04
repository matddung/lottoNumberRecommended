package com.studyjun.lotto.entitiy.oauth2;

import java.util.Map;

public class FacebookOAuth2MemberInfo extends OAuth2MemberInfo {
    public FacebookOAuth2MemberInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("name");
    }
}
