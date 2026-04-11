package esg.esgdocbuilder.model.dto.response;

import esg.esgdocbuilder.model.dto.RoleDTO;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserProfileResponse {
    private Long id;
    private String imageUrl;
    private String email;
    private String fullName;
    private String phone;
    private LocalDateTime createdAt;
    private List<RoleDTO> roles;
}
