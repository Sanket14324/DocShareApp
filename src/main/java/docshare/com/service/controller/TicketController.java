package docshare.com.service.controller;

import docshare.com.service.constant.Constant;
import docshare.com.service.model.Ticket;
import docshare.com.service.services.ITicketService;
import docshare.com.service.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TicketController {


    @Autowired
    private ITicketService ticketService;

    @Autowired
    private IUserService userService;


    @PostMapping("/ticket/create")
    public String createTicket(@ModelAttribute("ticketTitle") String ticketTitle, Authentication authentication, Model model){
        if(authentication == null){
            return "redirect:/login";
        }
        String result = ticketService.createTicket(authentication.getName().toString(), ticketTitle);
        model.addAttribute("user", userService.getUserByEmail(authentication.getName().toString()).get());
        if(result.equals("success")){
            model.addAttribute("ticketTitle",new String(""));
            return "redirect:/user/home";
        }
        return "/user/home";
    }

    @GetMapping("/ticket/all")
    public String getAllTicket(Model model,
                               Authentication authentication,
                               @RequestParam(name = "startDate", required = false)
                                   String startDate,
                               @RequestParam(name = "endDate", required = false)
                                  String endDate,
                               @RequestParam(name = "status", required = false) String status){
        if(authentication == null){
            return "redirect:/login";
        }
        model.addAttribute("user", userService.getUserByEmail(authentication.getName().toString()).get());
        List<Ticket> tickets = new ArrayList<>();

            // Otherwise, get all tickets
            tickets = ticketService.getAllTickets(startDate, endDate, status);

        model.addAttribute("ticketList", tickets);
        return "/admin/tickets";
    }

    @GetMapping("/tickets")
    public String getTicketsByUserId(@RequestParam("id") Long id, Model model, Authentication authentication){
        if(authentication == null){
            return "redirect:/login";
        }
        model.addAttribute("user",userService.getUserByEmail(authentication.getName().toString()).get());
        List<Ticket> ticketList = ticketService.getTicketsByUserId(id);
        model.addAttribute("ticketUser", userService.getUserById(id).get());
        model.addAttribute("userTicketList", ticketList);
        return "/admin/user-tickets";
    }


    @GetMapping("/tickets/update/{status}")
    public String changeStatus(Model model,@PathVariable("status") String status,@RequestParam("id") Long id,@RequestParam("userId") Long userId, Authentication authentication){
        if(authentication == null){
            return "redirect:/login";
        }
        ticketService.changeStatus(status, id);
        return "redirect:/tickets?id="+userId;
    }

    @GetMapping("/ticket/all/update/{status}")
    public String changeStatusOnHomePage(Model model,@PathVariable("status") String status,@RequestParam("id") Long id, Authentication authentication){
        if(authentication == null){
            return "redirect:/login";
        }
        model.addAttribute("user", userService.getUserByEmail(authentication.getName().toString()).get());
        ticketService.changeStatus(status, id);
        List<Ticket> tickets = ticketService.getAllTickets("", "", Constant.OPEN);
        model.addAttribute("ticketList", tickets);
        return "/admin/tickets";
    }

    @GetMapping("/tickets/delete")
    public String deleteTicketByTicketId(@RequestParam("id") Long id, Authentication authentication){
        if(authentication == null){
            return "redirect:/login";
        }
        ticketService.deleteTicket(id);
        return "redirect:/user/home";
    }
}
