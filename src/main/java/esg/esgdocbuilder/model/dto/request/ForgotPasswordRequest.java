package esg.esgdocbuilder.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @Email(message = "Email is required")
    @NotBlank(message = "Email can not be empty")
    private String email;
}
