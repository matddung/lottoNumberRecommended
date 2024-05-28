package com.studyjun.lotto.repository;

import com.studyjun.lotto.entitiy.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByAccount(String account);
    Optional<Member> findByEmailAndAccount(String email, String account);
    boolean findByEmail(String email);
}