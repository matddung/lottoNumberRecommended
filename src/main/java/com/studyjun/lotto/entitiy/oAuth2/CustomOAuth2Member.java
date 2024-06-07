package com.studyjun.lotto.entitiy.oAuth2;

import com.studyjun.lotto.common.MemberType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2Member extends DefaultOAuth2User {

    private String email;
    private MemberType type;

    public CustomOAuth2Member(Collection<? extends GrantedAuthority> authorities,
                              Map<String, Object> attributes,
                              String nameAttributeKey,
                              String email,
                              MemberType type) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.type = type;
    }
}