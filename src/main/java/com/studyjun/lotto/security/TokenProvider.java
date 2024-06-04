package com.studyjun.lotto.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyjun.lotto.entitiy.MemberRefreshToken;
import com.studyjun.lotto.repository.MemberRefreshTokenRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Getter
@Service
public class TokenProvider {
    @Value("${jwt.secret-key}")
    private String secretKey;

    private final long expirationMinutes;
    private final long refreshExpirationHours;
    private final String issuer;
    private final String accessHeader;
    private final String refreshHeader;
    private final long reissueLimit;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public TokenProvider(
            @Value("${jwt.access.expiration-minutes}") long expirationMinutes,
            @Value("${jwt.refresh.expiration-hours}") long refreshExpirationHours,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.access.header}") String accessHeader,
            @Value("${jwt.refresh.header}") String refreshHeader,
            MemberRefreshTokenRepository memberRefreshTokenRepository) {
        this.expirationMinutes = expirationMinutes;
        this.refreshExpirationHours = refreshExpirationHours;
        this.issuer = issuer;
        this.accessHeader = accessHeader;
        this.refreshHeader = refreshHeader;
        this.memberRefreshTokenRepository = memberRefreshTokenRepository;
        this.reissueLimit = refreshExpirationHours * 60 / expirationMinutes;
    }

    // 액세스 토큰을 만듬
    public String createAccessToken(String userSpecification) {
        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                .setSubject(userSpecification)
                .setIssuer(issuer)
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(Date.from(Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES)))
                .compact();
    }

    // 리프레시 토큰을 만듬
    public String createRefreshToken() {
        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                .setIssuer(issuer)
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(Date.from(Instant.now().plus(refreshExpirationHours, ChronoUnit.HOURS)))
                .compact();
    }

    // 주어진 토큰을 검증하고, 토큰의 주체(subject)를 반환, 토큰을 파싱하여 클레임을 추출한 후 주체를 반환
    public String validateTokenAndGetSubject(String token) {
        return validateAndParseToken(token)
                .getBody()
                .getSubject();
    }

    public void updateRefreshToken(String email, String refreshToken) {
        memberRefreshTokenRepository.findMemberByEmail(email)
                .ifPresentOrElse(
                        member -> member.updateRefreshToken(refreshToken),
                        () -> new Exception("일치하는 회원이 없습니다.")
                );
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    // 기존 액세스 토큰을 기반으로 새로운 액세스 토큰을 재발급
    @Transactional
    public String recreateAccessToken(String oldAccessToken) throws JsonProcessingException {
        String subject = decodeJwtPayloadSubject(oldAccessToken);
        memberRefreshTokenRepository.findByMemberIdAndReissueCountLessThan(UUID.fromString(subject.split(":")[0]), reissueLimit)
                .ifPresentOrElse(
                        MemberRefreshToken::increaseReissueCount,
                        () -> { throw new ExpiredJwtException(null, null, "Refresh token expired."); }
                );
        return createAccessToken(subject);
    }

    // 주어진 리프레시 토큰과 기존 액세스 토큰을 검증
    @Transactional(readOnly = true)
    public void validateRefreshToken(String refreshToken, String oldAccessToken) throws JsonProcessingException {
        validateAndParseToken(refreshToken);
        String memberId = decodeJwtPayloadSubject(oldAccessToken).split(":")[0];
        memberRefreshTokenRepository.findByMemberIdAndReissueCountLessThan(UUID.fromString(memberId), reissueLimit)
                .filter(memberRefreshToken -> memberRefreshToken.validateRefreshToken(refreshToken))
                .orElseThrow(() -> new ExpiredJwtException(null, null, "Refresh token expired."));
    }

    // 주어진 토큰을 검증하고, 클레임을 파싱하여 반환
    private Jws<Claims> validateAndParseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token);
    }

    // 주어진 액세스 토큰의 페이로드를 디코딩하고, 주체(subject)를 추출하여 반환
    private String decodeJwtPayloadSubject(String oldAccessToken) throws JsonProcessingException {
        return objectMapper.readValue(
                new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]), StandardCharsets.UTF_8),
                Map.class
        ).get("sub").toString();
    }
}