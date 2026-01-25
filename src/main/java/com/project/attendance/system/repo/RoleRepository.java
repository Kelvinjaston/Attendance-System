package com.project.attendance.system.repo;

import com.project.attendance.system.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
     Optional<Role> findByName(String name);
}
