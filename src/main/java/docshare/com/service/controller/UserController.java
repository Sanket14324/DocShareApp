package docshare.com.service.controller;


import docshare.com.service.dto.Role;
import docshare.com.service.dto.UserDto;
import docshare.com.service.model.Ticket;
import docshare.com.service.model.User;
import docshare.com.service.model.UserForm;
import docshare.com.service.services.ITicketService;
import docshare.com.service.services.IUserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller

public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ITicketService ticketService;

    @Autowired
    private IUserService userService;


    @GetMapping("/register")
    public String registerUser(Model model){
        model.addAttribute("userForm", new UserForm());
        return "register";
    }
    @PostMapping("/register")
    public String saveUser(@ModelAttribute(name = "userForm") @Valid UserForm userform, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "register"; // Return to the registration form with validation errors
        }
        User user = new User();
        user.setFirstName(userform.getFirstName());
        user.setLastName(userform.getLastName());
        user.setEmail(userform.getEmail());
        user.setPassword(userform.getPassword());
        user.setMobile(userform.getMobile());
        user.setRole(Role.USER);
        User savedUser = userService.registerUser(user);
        return "login";
    }


    @GetMapping("/")
    public String getHomePage(Authentication authentication){
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                return "redirect:/admin/home"; // Redirect to /admin for users with ADMIN authority
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
                return "redirect:/user/home"; // Redirect to /user for users with USER authority
            }
        }
        return "index";
    }

    @GetMapping("/user/home")
    public String getUserHome(Model model, Authentication authentication){
        if(authentication == null){
            return "redirect:/login";
        }
        model.addAttribute("user", userService.getUserByEmail(authentication.getName().toString()).get());
        List<Ticket> tickets = ticketService.getTicketByUserName(authentication.getName().toString());
        model.addAttribute("userTicketList", tickets);
        return "/user/home";
    }
    @GetMapping("/admin/home")
    public String getAllUser(Model model, Authentication authentication){
        if(authentication == null){
            return "redirect:/login";
        }
        List<User> userList = userService.getUserList();
        model.addAttribute("user", userService.getUserByEmail(authentication.getName().toString()).get());
        ModelMapper modelMapper = new ModelMapper();
        List<UserDto> userDtoList = new ArrayList<>();
        for(User user: userList){
            userDtoList.add(modelMapper.map(user, UserDto.class) );
        }
        model.addAttribute("userList", userDtoList);
        return "/admin/home";

    }


    @GetMapping("/email/{email}")

    public ResponseEntity getUserByEmailOrId(@PathVariable String email){

        Optional<User> user = userService.getUserByEmail(email);

        ModelMapper modelMapper = new ModelMapper();

        UserDto userDto = modelMapper.map(user.get(), UserDto.class);
        return  ResponseEntity.of(Optional.of(userDto));


    }





    @GetMapping("/admin/delete/user/{id}")
    public String deleteUserById(@PathVariable("id") Long id, Authentication authentication){
        if(authentication == null){
            return "redirect:/login";
        }
        String deletedUser = userService.deleteUser(id);
        return "redirect:/admin/home";

    }


    @RequestMapping("/login")
    public String getLoginForm(Model model, Authentication authentication){

        if(authentication != null && authentication.isAuthenticated()){
            return "redirect:/";
        }
        return "login";
    }

}
