package eshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String getNotFoundPage(NoHandlerFoundException e) {
        return "404";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException(final UserNotFoundException exception) {
        final ModelAndView content = new ModelAndView("register");

        content.addObject("userNotFoundError", exception.getMessage());

        return content;
    }
}

