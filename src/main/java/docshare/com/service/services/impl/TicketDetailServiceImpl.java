package docshare.com.service.services.impl;

import docshare.com.service.model.Ticket;
import docshare.com.service.model.TicketDetail;
import docshare.com.service.model.User;
import docshare.com.service.repository.TicketDetailsRepository;
import docshare.com.service.services.ITicketDetailService;
import docshare.com.service.services.ITicketService;
import docshare.com.service.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class TicketDetailServiceImpl implements ITicketDetailService {

    @Autowired
    private ITicketService ticketService;

    @Autowired
    private IUserService userService;
    @Autowired
    private TicketDetailsRepository ticketDetailsRepository;



    @Override
    public List<TicketDetail> getAllTicketDetailsByTicketId(Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        return  ticketDetailsRepository.findAllByTicket(ticket);
    }


    @Override
    public String createTicketComment(String comment, Long ticketId, String username) {
        User user = userService.getUserByEmail(username).get();
        Ticket ticket = ticketService.getTicketById(ticketId);
       TicketDetail ticketDetail = new TicketDetail();
       ticketDetail.setComment(comment);
       ticketDetail.setUser(user);
       ticketDetail.setTicket(ticket);
       ticketDetail.setCreatedDate(new Timestamp(new Date().getTime()));
       ticketDetailsRepository.save(ticketDetail);
        return "success";
    }
}
