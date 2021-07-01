package ua.svinkov.facultative.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.svinkov.facultative.entity.Role;
import ua.svinkov.facultative.entity.User;

/**
 * Interface for manage users
 * 
 * @author R. Svinkov
 *
 */
public interface UserService {

	/**
	 * Find user entity in DB by login
	 * 
	 * @param login Login of user
	 * @return User entity
	 */
	User findUserByLogin(String login);

	/**
	 * Find User by its id
	 * 
	 * @param id Identity of users
	 * @return Optional of User entity
	 */
	Optional<User> findUserById(Long id);

	/**
	 * Create new user
	 * 
	 * @param user User entity
	 * @return User entity
	 */
	User create(User user);

	/**
	 * Get all users by role
	 * 
	 * @param id Role id
	 * @return List of users
	 */
	List<User> findAllByRole(Long id);

	/**
	 * Get all users defined by page
	 * 
	 * @param page Page parametrs
	 * @return Page of users
	 */
	Page<User> findAllUsers(Pageable page);

	/**
	 * Get all roles
	 * 
	 * @return List of roles
	 */
	List<Role> findAllRoles();
}
