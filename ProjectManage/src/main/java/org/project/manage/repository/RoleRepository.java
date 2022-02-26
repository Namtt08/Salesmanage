package org.project.manage.repository;

import java.util.Optional;

import org.project.manage.entities.Role;
import org.project.manage.security.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);

}
