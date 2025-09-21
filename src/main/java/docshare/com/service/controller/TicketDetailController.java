package docshare.com.service.controller;

import docshare.com.service.constant.Constant;
import docshare.com.service.model.Ticket;
import docshare.com.service.model.TicketDetail;
import docshare.com.service.services.ITicketDetailService;
import docshare.com.service.services.ITicketService;
import docshare.com.service.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

@Controller
public class TicketDetailController {


    @Autowired
    private ITicketDetailService ticketDetailService;

    @Autowired
    private IUserService userService;
    @Autowired
    private ITicketService ticketService;
    @PostMapping("/ticketDetail/create/{ticketId}")
    public String createTicketDetail(Model model, @PathVariable("ticketId") Long ticketId, Authentication authentication, @ModelAttribute("comment") String comment){
        if(authentication == null){
            return "redirect:/login";
        }
        model.addAttribute("user", userService.getUserByEmail(authentication.getName().toString()).get());
        String result =  ticketDetailService.createTicketComment(comment, ticketId, authentication.getName().toString());
        model.addAttribute("comment", new String(""));
        return "redirect:/ticketDetail?id="+ticketId;
    }

    @GetMapping("/ticketDetail")
    public String getAllComments(Model model, @RequestParam("id") Long ticketId, Authentication authentication){
        if(authentication == null){
            return "redirect:/login";
        }
        model.addAttribute("user", userService.getUserByEmail(authentication.getName().toString()).get());
        model.addAttribute("ticketDetail", ticketService.getTicketById(ticketId));
        model.addAttribute("fileList", ticketService.getTicketFilesByTicketId(ticketId));
        List<TicketDetail> commentList = ticketDetailService.getAllTicketDetailsByTicketId(ticketId);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            model.addAttribute("adminCommentList", commentList);
            return "/admin/comments";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
            Ticket ticket = ticketService.getTicketById(ticketId);
            if(ticket.getUser().getEmail().equals(authentication.getName().toString())){
                model.addAttribute("userCommentList", commentList);
                return "/user/comments";
            }
        }
        model.addAttribute("error", "Unauthorized to access ticket details");
        return "redirect:/user/home";
    }


    @PostMapping("/ticketDetail/file/{ticketId}")
    public String createTicketDetailWithFile(Model model, @PathVariable("ticketId") Long ticketId, Authentication authentication, @RequestParam("files") List<MultipartFile> files) {
        if(authentication == null){
            return "redirect:/login";
        }
        if(files.get(0).getOriginalFilename().equals("") || files.get(0).getOriginalFilename() == null || files.get(0).getOriginalFilename().isEmpty() ||files.get(0).getOriginalFilename().isBlank()){
            model.addAttribute("error", "Please select The file");
            model.addAttribute("user", userService.getUserByEmail(authentication.getName().toString()).get());
            model.addAttribute("ticketDetail", ticketService.getTicketById(ticketId));
            model.addAttribute("fileList", ticketService.getTicketFilesByTicketId(ticketId));
            List<TicketDetail> commentList = ticketDetailService.getAllTicketDetailsByTicketId(ticketId);
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                model.addAttribute("adminCommentList", commentList);
                return "/admin/comments";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
                Ticket ticket = ticketService.getTicketById(ticketId);
                if(ticket.getUser().getEmail().equals(authentication.getName().toString())){
                    model.addAttribute("userCommentList", commentList);
                    return "/user/comments";
                }
            }
        }
        if(files.size() > 0) {
            for (MultipartFile file : files) {;
                    ticketService.uploadFileByTicketId(ticketId, file);
            }
        }
        return "redirect:/ticketDetail?id="+ticketId;
    }


    @GetMapping("/ticketDetail/file/delete/{fileId}")
    public String getDeleteTheFileByFileId(Model model, @RequestParam("id") Long ticketId, Authentication authentication, @PathVariable("fileId") Long fileId){
        if(authentication == null){
            return "redirect:/login";
        }
        model.addAttribute("user", userService.getUserByEmail(authentication.getName().toString()).get());
        model.addAttribute("ticketDetail", ticketService.getTicketById(ticketId));

        //deleting the file
        String result = ticketService.deleteTheFile(fileId);
        if(result.equals(Constant.SUCCESS)){
            return "redirect:/ticketDetail?id="+ticketId;
        }

        model.addAttribute("error", "Error While deleting file");
        model.addAttribute("fileList", ticketService.getTicketFilesByTicketId(ticketId));
        List<TicketDetail> commentList = ticketDetailService.getAllTicketDetailsByTicketId(ticketId);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            model.addAttribute("adminCommentList", commentList);
            return "/admin/comments";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
            Ticket ticket = ticketService.getTicketById(ticketId);
            if(ticket.getUser().getEmail().equals(authentication.getName().toString())){
                model.addAttribute("userCommentList", commentList);
                return "/user/comments";
            }
        }
        model.addAttribute("error", "Unauthorized to access ticket details");
        return "redirect:/user/home";
    }

}
