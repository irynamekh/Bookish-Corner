package com.bookstore.repository;

import com.bookstore.model.Role;
import com.bookstore.model.Role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getByRoleName(RoleName roleName);
}
