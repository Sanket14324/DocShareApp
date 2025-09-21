package docshare.com.service.model;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
public class LoginForm {

    @Email(message = "email must be in proper format")
    @NotNull(message = "must not null")
    private String email;

    @NotNull(message = "must not null")
    private String password;

}
