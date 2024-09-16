package project7.userservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project7.userservice.model.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String profileImage;
    private Role role;
}
