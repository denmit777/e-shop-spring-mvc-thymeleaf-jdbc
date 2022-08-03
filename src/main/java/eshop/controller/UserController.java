package eshop.controller;

import eshop.dto.UserDto;
import eshop.model.User;
import eshop.service.CartService;
import eshop.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private static final Logger LOGGER = LogManager.getLogger(UserController.class.getName());

    private final UserService userService;
    private final CartService cartService;

    public UserController(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String saveUser(Model model, @ModelAttribute UserDto userDto, HttpSession session) {
        if (userService.isInvalidUser(userDto)) {
            String errors = userService.invalidUser(userDto);

            model.addAttribute("validationErrors", errors);

            return "register";
        }

        User user = userService.save(userDto);

        session.setAttribute("userId", user.getId());
        session.setAttribute("login", user.getLogin());

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String start() {
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, @ModelAttribute UserDto userDto, HttpSession session) {
        String login = userDto.getLogin();
        String password = userDto.getPassword();

        User user = userService.getByLoginAndPassword(login, password);

        LOGGER.info(user);

        cartService.updateData(session);

        return clickingActions(request, session, user);
    }

    private String clickingActions(HttpServletRequest request, HttpSession session, User user) {
        if (request.getParameter("submit").equals("Enter")) {

            session.setAttribute("userId", user.getId());
            session.setAttribute("login", user.getLogin());

            return eventsWithCheckbox(request);
        }

        return "redirect:/register";
    }

    private String eventsWithCheckbox(HttpServletRequest request) {
        if (request.getParameter("isUserCheck") != null) {
            return "redirect:/goods";
        } else {
            return "error";
        }
    }
}

