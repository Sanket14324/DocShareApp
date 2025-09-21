package docshare.com.service.repository;

import docshare.com.service.model.Ticket;
import docshare.com.service.model.TicketDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketDetailsRepository extends JpaRepository<TicketDetail, Long> {

    List<TicketDetail> findAllByTicket(Ticket ticket);
}
