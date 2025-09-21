package docshare.com.service.model;

import docshare.com.service.dto.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user_table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @Size(min = 3, max = 30, message = "Name must contain min 3 characters or max 30 characters")
    private String firstName;

    @NotNull
    @Size(min = 3, max = 30, message = "Name must contain min 3 characters or max 30 characters")
    private String lastName;

    @NotNull
    @Email(message = "Email must be in proper format")
    private String email;

    @NotNull
    @Size(min= 8, message = "Password must be contain 8 or more than 8 character")
    private String password;

    @NotNull(message = "User must have mobile number")
    private String mobile;


    @NotNull(message = "User must have role")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TicketDetail> ticketDetails;
}
