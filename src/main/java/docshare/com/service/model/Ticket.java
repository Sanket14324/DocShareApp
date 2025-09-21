package docshare.com.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "created_date")
    private java.sql.Timestamp createdDate;

    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<TicketDetail> ticketDetails;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<TicketDetailFile> ticketDetailFiles;
}
