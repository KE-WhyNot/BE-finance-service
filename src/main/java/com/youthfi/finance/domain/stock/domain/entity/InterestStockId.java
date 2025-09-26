package com.youthfi.finance.domain.stock.domain.entity;

import com.youthfi.finance.domain.user.domain.entity.User;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class InterestStockId implements Serializable {

    private Long user; // User의 userId
    private String stock; // Stock의 stockId

    public InterestStockId(User user, Stock stock) {
        this.user = user.getUserId();
        this.stock = stock.getStockId();
    }
}
