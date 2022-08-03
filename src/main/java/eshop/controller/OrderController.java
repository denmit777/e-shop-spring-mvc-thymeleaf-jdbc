package eshop.controller;

import eshop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/order")
    public String getOrder(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        BigDecimal totalPrice = (BigDecimal) session.getAttribute("totalPrice");

        model.addAttribute("login", session.getAttribute("login"));
        model.addAttribute("orderList", session.getAttribute("order"));
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("orderResult", orderService.orderResult(totalPrice));

        orderService.save(userId, totalPrice);

        return "order";
    }
}
