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
        
        return new UserResponse(
                user.getUserId(),
                user.getBalance(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
