package docshare.com.service.services;

import docshare.com.service.model.Ticket;
import docshare.com.service.model.TicketDetailFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ITicketService {

    List<Ticket> getAllTickets(String startDate, String endDate, String status);

    List<Ticket> getTicketByUserName(String username);

    String createTicket(String username, String title);

    List<Ticket> getTicketsByUserId(Long userId);

    Ticket getTicketById(Long id);

    List<TicketDetailFile> getTicketFilesByTicketId(Long id);

    String uploadFileByTicketId(Long id, MultipartFile file);

    String deleteTheFile(Long fileId);

    String changeStatus(String status, Long ticketId);

    List<Ticket> filterTickets(String startDate, String endDate, String status);

    void deleteTicket(Long id);
}

