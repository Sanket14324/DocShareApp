package docshare.com.service.services;
import docshare.com.service.model.TicketDetail;

import java.util.*;
public interface ITicketDetailService {

    List<TicketDetail> getAllTicketDetailsByTicketId(Long id);

    String createTicketComment(String comment, Long ticketId, String username);



}
