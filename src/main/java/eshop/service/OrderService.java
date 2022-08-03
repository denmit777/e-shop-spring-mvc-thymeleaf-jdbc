package eshop.service;

import java.math.BigDecimal;

public interface OrderService {

    void save(Long userId, BigDecimal totalPrice);

    String orderResult(BigDecimal totalPrice);
}

