package docshare.com.service.services;

import docshare.com.service.model.Ticket;
import docshare.com.service.model.TicketDetailFile;

import java.util.List;

public interface ITicketDetailFileService {


    String createTicketFile(TicketDetailFile ticketDetailFile);

    List<TicketDetailFile> getTicketFilesByTicketId(Ticket ticket);

    String deleteTicketFile(Long id);

    TicketDetailFile getTicketFileById(Long id);
}
