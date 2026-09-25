package com.example.backend.service;

import com.example.backend.entity.Role;
import com.example.backend.exception.ResourceAlreadyExistsException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

  private final RoleRepository roleRepository;

  public RoleService(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  public Role createRole(String name) {

    if (roleRepository.existsByName(name)) {
      throw new ResourceAlreadyExistsException(
          "Role already exists");
    }

    Role role = new Role();
    role.setName(name);

    return roleRepository.save(role);
  }

  public Role getRoleByName(String name) {

    return roleRepository.findByName(name)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Role not found"));
  }
}
