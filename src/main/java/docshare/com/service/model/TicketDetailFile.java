package docshare.com.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetailFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String location;

    @Column(name = "created_date")
    private java.sql.Timestamp createdDate;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    // Getters and setters
}
