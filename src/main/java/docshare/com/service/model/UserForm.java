package docshare.com.service.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserForm {

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


}
