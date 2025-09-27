package com.youthfi.finance.domain.user.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.user.domain.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 기본 조회
    Optional<User> findByUserId(Long userId);
    
    // 이름으로 검색
    List<User> findByNameContaining(String name);
}
