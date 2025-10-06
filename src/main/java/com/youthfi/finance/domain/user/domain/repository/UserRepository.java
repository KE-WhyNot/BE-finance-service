package com.youthfi.finance.domain.user.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.user.domain.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    // 기본 조회
    Optional<User> findByUserId(String userId);
}
