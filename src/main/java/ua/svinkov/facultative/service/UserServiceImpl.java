package ua.svinkov.facultative.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ua.svinkov.facultative.entity.Role;
import ua.svinkov.facultative.entity.User;
import ua.svinkov.facultative.repository.RoleRepository;
import ua.svinkov.facultative.repository.UserRepository;
import ua.svinkov.facultative.util.Constants;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public User findUserByLogin(String login) {
		return userRepository.findUserByLoginIgnoreCase(login);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findUserByLogin(username);

		if (!user.isEnabled()) {
			throw new DisabledException("Account blocked!");
		}

		String roleNames = user.getRole().getName();

		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			GrantedAuthority authority = new SimpleGrantedAuthority(roleNames);
			grantList.add(authority);
		}

		UserDetails userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(user.getLogin(),
				user.getPassword(), grantList);

		return userDetails;
	}

	@Override
	public User create(User user) {
		requireNonNull(user, "User must be not null");
		return userRepository.save(user);
	}

	@Override
	public List<User> findAllByRole(Long id) {
		return userRepository.findAllByRoleId(id);
	}

//
	@Override
	public Page<User> findAllUsers(Optional<Integer> page, Optional<Integer> size) {
		return userRepository.findAll(PageRequest.of(page.orElse(Constants.DEFAULT_CURRENT_PAGE) - 1,
				size.orElse(Constants.DEFAULT_PAGE_SIZE)));
	}

	@Override
	public Optional<User> findUserById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public List<Role> findAllRoles() {
		return roleRepository.findAll();
	}

}
