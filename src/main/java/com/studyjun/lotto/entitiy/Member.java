package com.studyjun.lotto.entitiy;

import com.studyjun.lotto.common.MemberType;
import com.studyjun.lotto.common.SocialType;
import com.studyjun.lotto.dto.member.request.MemberUpdateRequest;
import com.studyjun.lotto.dto.signUp.request.SignUpRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, scale = 20, unique = true)
    private String account;
    @Column(nullable = false)
    private String password;
    private String nickname;
    private String birth;
    @Enumerated(EnumType.STRING)
    private MemberType type;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    private String socialId;

    @Builder
    private Member(String account, String password, String nickname, String birth, MemberType type, String email, String phoneNumber, SocialType socialType, String socialId) {
        this.account = account;
        this.password = password;
        this.nickname = nickname;
        this.birth = birth;
        this.type = type;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.socialType = socialType;
        this.socialId = socialId;
    }

    public static Member from(SignUpRequest request, PasswordEncoder encoder) {
        return Member.builder()
                .account(request.account())
                .password(encoder.encode(request.password()))
                .nickname(request.nickname())
                .birth(request.birth())
                .type(MemberType.USER)
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .socialId("NONE")
                .socialType(SocialType.NONE)
                .build();
    }

    public void update(MemberUpdateRequest newMember, PasswordEncoder encoder) {
        this.password = newMember.newPassword() == null || newMember.newPassword().isBlank()
                ? this.password : encoder.encode(newMember.newPassword());
        this.nickname = newMember.nickname();
        this.email = newMember.email();
        this.phoneNumber = newMember.phoneNumber();
    }
}