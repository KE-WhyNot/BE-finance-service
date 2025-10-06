package com.youthfi.finance.domain.user.application.mapper;

import com.youthfi.finance.domain.user.application.dto.response.UserResponse;
import com.youthfi.finance.domain.user.domain.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User 도메인 관련 Entity ↔ DTO 변환 Mapper
 */
@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        
        return UserResponse.builder()
                .userId(user.getUserId())
                .balance(user.getBalance())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
