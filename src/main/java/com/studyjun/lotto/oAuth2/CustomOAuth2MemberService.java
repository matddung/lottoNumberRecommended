package com.studyjun.lotto.oAuth2;

import com.studyjun.lotto.common.SocialType;
import com.studyjun.lotto.entitiy.Member;
import com.studyjun.lotto.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";
    private static final String FACEBOOK = "facebook";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2Attributes extractAttributes = OAuth2Attributes.of(socialType, userNameAttributeName, attributes);

        Member createdUser = getUser(extractAttributes, socialType);

        return new CustomOAuth2Member(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getType().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdUser.getEmail(),
                createdUser.getType()
        );
    }

    private SocialType getSocialType(String registrationId) {
        if (NAVER.equals(registrationId)) return SocialType.NAVER;
        if (KAKAO.equals(registrationId)) return SocialType.KAKAO;
        if (FACEBOOK.equals(registrationId)) return SocialType.FACEBOOK;
        return SocialType.GOOGLE;
    }

    private Member getUser(OAuth2Attributes attributes, SocialType socialType) {
        Member findMember = memberRepository.findBySocialTypeAndSocialId(socialType,
                attributes.getOAuth2MemberInfo().getId()).orElse(null);

        if (findMember == null) return saveUser(attributes, socialType);

        return findMember;
    }

    private Member saveUser(OAuth2Attributes attributes, SocialType socialType) {
        Member createdMember = attributes.toEntity(socialType, attributes.getOAuth2MemberInfo());
        return memberRepository.save(createdMember);
    }
}