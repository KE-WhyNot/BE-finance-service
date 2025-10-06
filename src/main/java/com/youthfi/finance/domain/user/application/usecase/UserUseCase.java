package com.youthfi.finance.domain.user.application.usecase;

import com.youthfi.finance.domain.user.application.dto.response.*;
import com.youthfi.finance.domain.user.application.mapper.UserMapper;
import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import com.youthfi.finance.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserUseCase {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * 사용자 생성 시나리오 (X-User-Id에서 받은 userId 사용)
     */
    @Transactional
    public UserResponse createUser(String userId) {

        User user = userService.createUser(userId);
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    /**
     * 사용자 잔고 조회 (본인만)
     */
    public UserResponse getMyBalance(String userId) {

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return userMapper.toUserResponse(user);
    }



    /**
     * 사용자 존재 여부 확인
     */
    public boolean existsUser(String userId) {
        return userService.existsUser(userId);
    }



}
