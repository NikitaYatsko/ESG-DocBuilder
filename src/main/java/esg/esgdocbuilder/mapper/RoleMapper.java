package esg.esgdocbuilder.mapper;

import esg.esgdocbuilder.model.dto.RoleDTO;
import esg.esgdocbuilder.model.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleDTO toDto(Role role) {
        if (role == null) return null;

        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        return dto;
    }
}
