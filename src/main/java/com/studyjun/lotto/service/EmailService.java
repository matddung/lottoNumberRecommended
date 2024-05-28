package com.studyjun.lotto.service;

import com.studyjun.lotto.dto.email.request.FindPasswordRequest;
import com.studyjun.lotto.dto.email.response.FindPasswordResponse;
import com.studyjun.lotto.entitiy.Member;
import com.studyjun.lotto.repository.MemberRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private static final String ADMIN_ADDRESS = "matddung76@naver.com";

    @Async
    @Transactional
    public FindPasswordResponse sendTempPasswordMail(FindPasswordRequest request) throws UnsupportedEncodingException, MessagingException {
        if (userEmailCheck(request.email(), request.account())) {
            String tempPassword = getTempString();
            MimeMessage message = mailSender.createMimeMessage();
            message.addRecipients(Message.RecipientType.TO, request.email());
            message.setSubject("[lotto 이메일 관련]");
            String text = "lotto 임시 비밀번호 안내 : " + tempPassword + " 입니다.";
            message.setText(text, "utf-8");
            message.setFrom(new InternetAddress(ADMIN_ADDRESS, request.email()));
            updatePassword(tempPassword, request.email(), request.account());
            mailSender.send(message);
            return new FindPasswordResponse(true, tempPassword, text);
        } else {
            return new FindPasswordResponse(false, getTempString(), "error");
        }
    }

    @Transactional(readOnly = true)
    public boolean userEmailCheck(String email, String account) {
        Member member = memberRepository.findByEmailAndAccount(email, account)
                .orElseThrow(() -> new IllegalArgumentException("입력하신 정보가 일치하지 않습니다."));
        return member != null;
    }

    @Transactional
    public String getTempString() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String str = "";
        int idx = 0;
        for (int i = 0; i < 8; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    @Transactional
    public void updatePassword(String str, String email, String account) {
        Member member = memberRepository.findByEmailAndAccount(email, account)
                .orElseThrow(() -> new IllegalArgumentException("입력하신 정보가 일치하지 않습니다."));
        member.setPassword(encoder.encode(str));
        memberRepository.save(member);
    }
}