package com.afridi.gamingbackend.domain.repository;



import com.afridi.gamingbackend.domain.model.Role;
import com.afridi.gamingbackend.domain.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    // Optional<Role> findByName(RoleName roleName);
    Optional<Role> findByName(RoleName role);
}
