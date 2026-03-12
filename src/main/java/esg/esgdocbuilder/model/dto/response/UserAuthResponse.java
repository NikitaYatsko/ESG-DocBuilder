package esg.esgdocbuilder.model.dto.response;

import esg.esgdocbuilder.model.dto.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthResponse {

    private String email;
    private String fullName;
    private String token;
    private List<RoleDTO> roles;
}