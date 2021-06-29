package ua.svinkov.facultative.repository;

import ua.svinkov.facultative.entity.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	User findUserByLoginIgnoreCase(String login);

	List<User> findAllByRoleId(Long id);
}
