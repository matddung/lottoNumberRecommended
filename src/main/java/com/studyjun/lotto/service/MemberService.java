package com.studyjun.lotto.service;

import com.studyjun.lotto.dto.member.request.MemberUpdateRequest;
import com.studyjun.lotto.dto.member.response.MemberDeleteResponse;
import com.studyjun.lotto.dto.member.response.MemberInfoResponse;
import com.studyjun.lotto.dto.member.response.MemberUpdateResponse;
import com.studyjun.lotto.repository.MemberRefreshTokenRepository;
import com.studyjun.lotto.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(UUID id) {
        return memberRepository.findById(id)
                .map(MemberInfoResponse::from)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));
    }

    @Transactional
    public MemberDeleteResponse deleteMember(UUID id) {
        if (!memberRepository.existsById(id)) return new MemberDeleteResponse(false);
        memberRefreshTokenRepository.deleteById(id);
        memberRepository.deleteById(id);
        return new MemberDeleteResponse(true);
    }

    @Transactional
    public MemberUpdateResponse updateMember(UUID id, MemberUpdateRequest request) {
        return memberRepository.findById(id)
                .filter(member -> encoder.matches(request.password(), member.getPassword()))
                .map(member -> {
                    member.update(request, encoder);
                    return MemberUpdateResponse.of(true, member);
                })
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
    }
}