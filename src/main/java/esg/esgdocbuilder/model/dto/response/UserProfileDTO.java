package esg.esgdocbuilder.model.dto.response;

import esg.esgdocbuilder.model.dto.RoleDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileDTO {
    private String email;
    private String fullName;
    private String token;
    private List<RoleDTO> roles;
}
