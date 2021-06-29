package ua.svinkov.facultative.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import ua.svinkov.facultative.entity.Role;
import ua.svinkov.facultative.entity.User;

public interface UserService {

	User findUserByLogin(String login);

	Optional<User> findUserById(Long id);

	User create(User user);

	List<User> findAllByRole(Long id);

	Page<User> findAllUsers(Optional<Integer> page, Optional<Integer> size);

	List<Role> findAllRoles();
}
