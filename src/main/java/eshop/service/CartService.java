package eshop.service;

import eshop.model.Cart;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

public interface CartService {

    void addGoodToCart(String option, Long userId);

    void deleteGoodFromCart(String option);

    void updateData(HttpSession session);

    String printChosenGoods(Cart cart);

    String printOrder(Cart cart);

    BigDecimal getTotalPrice(Cart cart);
}
