package docshare.com.service.repository;

import docshare.com.service.model.Ticket;
import docshare.com.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUser(User user);

    List<Ticket> findByCreatedDateBetweenAndStatus(Timestamp createdDate, Timestamp createdDate2, String status);

    List<Ticket> findByStatus(String status);
}
