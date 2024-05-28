package com.studyjun.lotto.entitiy;

import com.studyjun.lotto.common.MemberType;
import com.studyjun.lotto.dto.signUp.request.SignUpRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private String name;
    private String birth;
    @Enumerated(EnumType.STRING)
    private MemberType type;
    @CreationTimestamp
    private LocalDate createdAt;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Builder
    private Member(String account, String password, String name, String birth, MemberType type, LocalDate createdAt, String email, String phoneNumber) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.type = type;
        this.createdAt = createdAt;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static Member from(SignUpRequest request, PasswordEncoder encoder) {
        return Member.builder()
                .account(request.account())
                .password(encoder.encode(request.password()))
                .name(request.name())
                .birth(request.birth())
                .type(MemberType.USER)
                .createdAt(LocalDate.now())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .build();
    }
}