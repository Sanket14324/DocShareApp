package docshare.com.service.repository;

import docshare.com.service.model.Ticket;
import docshare.com.service.model.TicketDetailFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketDetailFileRepository extends JpaRepository<TicketDetailFile, Long> {


    List<TicketDetailFile> findByTicket(Ticket ticket);
}
