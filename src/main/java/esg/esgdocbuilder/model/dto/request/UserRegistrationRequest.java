package esg.esgdocbuilder.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRegistrationRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String password;
    private String confirmPassword;
    private String role;
}
