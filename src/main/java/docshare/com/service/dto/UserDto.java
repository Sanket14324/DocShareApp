package docshare.com.service.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;
    private  String role;
}
