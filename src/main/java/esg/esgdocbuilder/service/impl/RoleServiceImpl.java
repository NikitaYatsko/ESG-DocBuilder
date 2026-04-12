package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.mapper.RoleMapper;
import esg.esgdocbuilder.model.dto.RoleDTO;
import esg.esgdocbuilder.repository.RoleRepository;
import esg.esgdocbuilder.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;


    @Override
    public List<RoleDTO> getRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toDto).collect(Collectors.toList());
    }
}
