package com.studyjun.lotto.repository;

import com.studyjun.lotto.entitiy.MemberRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, UUID> {
    Optional<MemberRefreshToken> findByMemberIdAndReissueCountLessThan(UUID id, long count);

    @Query("SELECT mrt FROM MemberRefreshToken mrt WHERE mrt.member.email = :email")
    Optional<MemberRefreshToken> findMemberByEmail(@Param("email") String email);
}