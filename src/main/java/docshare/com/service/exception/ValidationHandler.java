package docshare.com.service.exception;

import docshare.com.service.model.UserForm;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
//@Controller

public class ValidationHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public String handleDuplicateEmailException(DuplicateEmailException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("userForm", new UserForm());
        return "register";
    }


    @ExceptionHandler(AwsException.class)
    public String handleAwsException(AwsException ex){
        System.out.println(ex.getMessage());
        System.out.println(ex.getCause());
        return "redirect:/";
    }
}