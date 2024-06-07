package com.studyjun.lotto.entitiy.oAuth2;

import com.studyjun.lotto.common.MemberType;
import com.studyjun.lotto.common.SocialType;
import com.studyjun.lotto.entitiy.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuth2Attributes {

    private String nameAttributeKey;
    private OAuth2MemberInfo oAuth2MemberInfo;

    @Builder
    public OAuth2Attributes(String nameAttributeKey, OAuth2MemberInfo oAuth2MemberInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2MemberInfo = oAuth2MemberInfo;
    }

    public static OAuth2Attributes of(SocialType socialType,
                                     String userNameAttributeName, Map<String, Object> attributes) {

        if (socialType == SocialType.NAVER) return ofNaver(userNameAttributeName, attributes);
        if (socialType == SocialType.KAKAO) return ofKakao(userNameAttributeName, attributes);
        if (socialType == SocialType.FACEBOOK) return ofFacebook(userNameAttributeName, attributes);
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2MemberInfo(new KakaoOAuth2MemberInfo(attributes))
                .build();
    }

    public static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2MemberInfo(new GoogleOAuth2MemberInfo(attributes))
                .build();
    }

    public static OAuth2Attributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2MemberInfo(new NaverOAuth2MemberInfo(attributes))
                .build();
    }

    public static OAuth2Attributes ofFacebook(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2MemberInfo(new FacebookOAuth2MemberInfo(attributes))
                .build();
    }

    public Member toEntity(SocialType socialType, OAuth2MemberInfo oAuth2MemberInfo) {
        return Member.builder()
                .socialType(socialType)
                .socialId(oAuth2MemberInfo.getId())
                .account(UUID.randomUUID() + "ID")
                .password(UUID.randomUUID() + "pw")
                .email(UUID.randomUUID() + "@socialUser.com")
                .nickname(oAuth2MemberInfo.getNickname())
                .phoneNumber(UUID.randomUUID() + "수정이 필요합니다.")
                .type(MemberType.GUEST)
                .build();
    }
}