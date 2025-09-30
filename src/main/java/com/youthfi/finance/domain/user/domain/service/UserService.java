package com.youthfi.finance.domain.user.domain.service;

import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 생성
     */
    @Transactional
    public User createUser(String name, Long seedMoney, String profileImage) {
        User user = User.builder()
                .name(name)
                .seedMoney(BigDecimal.valueOf(seedMoney))
                .balance(BigDecimal.valueOf(seedMoney)) // 초기 잔고 = 시드머니
                .profileImage(profileImage)
                .build();
        
        return userRepository.save(user);
    }

    /**
     * 사용자 조회 (ID)
     */
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * 사용자 조회 (이름)
     */
    public List<User> getUsersByName(String name) {
        return userRepository.findByNameContaining(name);
    }

    /**
     * 모든 사용자 조회
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 사용자 정보 수정
     */
    @Transactional
    public User updateUser(Long userId, String name, String profileImage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));
        
        user.updateProfile(name, profileImage);
        return userRepository.save(user);
    }

    /**
     * 사용자 삭제
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));
        
        userRepository.delete(user);
    }

    /**
     * 잔고 업데이트
     */
    @Transactional
    public User updateBalance(Long userId, Long newBalance) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));
        
        user.updateBalance(BigDecimal.valueOf(newBalance));
        return userRepository.save(user);
    }

    /**
     * 사용자 존재 여부 확인
     */
    public boolean existsUser(Long userId) {
        return userRepository.existsById(userId);
    }
}
