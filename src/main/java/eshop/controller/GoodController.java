package eshop.controller;

import eshop.model.Cart;
import eshop.model.Good;
import eshop.service.CartService;
import eshop.service.GoodService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class GoodController {
    private static final Logger LOGGER = LogManager.getLogger(GoodController.class.getName());

    private final GoodService goodService;
    private final CartService cartService;

    private Cart cart;

    public GoodController(GoodService goodService, CartService cartService) {
        this.goodService = goodService;
        this.cartService = cartService;
    }

    @PostConstruct
    public void init() {
        cart = new Cart();
    }

    @GetMapping("/goods")
    public String getAllGoods(Model model, HttpSession session) {
        List<Good> goods = goodService.getAll();

        String options = goodService.getStringOfOptionsForDroppingMenuFromGoodList(goods);
        String chosenGoods = (String) session.getAttribute("chosenGoods");
        String choice = goodService.getChoice(chosenGoods);

        session.setAttribute("options", options);

        model.addAttribute("login", session.getAttribute("login"));
        model.addAttribute("options", options);
        model.addAttribute("choice", choice);

        return "goods";
    }

    @PostMapping("/createCart")
    public String createCart(HttpServletRequest request, HttpSession session) throws IOException, ServletException {
        createCart(session);

        String command = request.getParameter("submit");

        return clickingActions(command, request, session);
    }

    private void createCart(HttpSession session) {
        if (session.getAttribute("cart") != null) {
            cart = (Cart) session.getAttribute("cart");
        }
    }

    private String clickingActions(String command, HttpServletRequest request, HttpSession session) throws IOException, ServletException {
        BigDecimal totalPrice = cartService.getTotalPrice(cart);

        session.setAttribute("cart", cart);
        session.setAttribute("totalPrice", totalPrice);

        String option = goodService.getStringOfNameAndPriceFromOptionMenu(request.getParameter("goodName"));
        Long userId = (Long) session.getAttribute("userId");

        switch (command) {
            case "Add Goods":
                cartService.addGoodToCart(option, userId);

                getChosenGoods(session);

                return "redirect:/goods";
            case "Remove Goods":
                cartService.deleteGoodFromCart(option);

                getChosenGoods(session);

                return "redirect:/goods";
            case "Submit":
                String order = cartService.printOrder(cart);

                session.setAttribute("order", order);

                return "redirect:/order";
        }
        return "redirect:/login";
    }

    private void getChosenGoods(HttpSession session) {
        String chosenGoods = cartService.printChosenGoods(cart);

        LOGGER.info("Chosen goods: {}", chosenGoods);

        session.setAttribute("chosenGoods", chosenGoods);
    }
}
