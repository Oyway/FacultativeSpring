package ua.svinkov.facultative.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.svinkov.facultative.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
