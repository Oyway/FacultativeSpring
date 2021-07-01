package ua.svinkov.facultative.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import ua.svinkov.facultative.entity.Role;
import ua.svinkov.facultative.entity.User;
import ua.svinkov.facultative.repository.RoleRepository;
import ua.svinkov.facultative.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
	
	@InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository userRep;
    
    @Mock
    private RoleRepository roleRep;

	@Test
	void testFindUserByLogin() {
		User user = new User();
        user.setId(20L);
        user.setLogin("login");;

        when(userRep.findUserByLoginIgnoreCase("login")).thenReturn(user);

        service.findUserByLogin("login");

        verify(userRep, times(1)).findUserByLoginIgnoreCase("login");
	}

	@Test
	void testCreate() {
		User user = new User();
		user.setId(20L);
		user.setStatus(true);

		when(userRep.save(user)).thenReturn(user);

		service.create(user);

		verify(userRep, times(1)).save(user);
	}
	
	@Test
	void testFindAllByRole() {
		List<User> users = new ArrayList<>();
		users.add(new User());
		when(userRep.findAllByRoleId(1L)).thenReturn(users);

        service.findAllByRole(1L);

        verify(userRep, times(1)).findAllByRoleId(1L);
	}

	@Test
	void testFindAllUsers() {
		PageRequest pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.unsorted());
		List<User> users = Collections.emptyList();
		PageImpl<User> response = new PageImpl<>(users, pageable, 1);
		when(userRep.findAll(pageable)).thenReturn(response);

        service.findAllUsers(pageable);

        verify(userRep, times(1)).findAll(pageable);
		
	}

	@Test
	void testFindUserById() {
		Optional<User> user1 = Optional.of(User.builder().id(1L).build());

        when(userRep.findById(1L)).thenReturn(user1);

        service.findUserById(1L);

        verify(userRep, times(1)).findById(1L);

	}
	
	@Test
	void testFindUserById_IdNotExist() {
        when(userRep.findById(1L)).thenReturn(Optional.empty());

        service.findUserById(1L);

        verify(userRep, times(1)).findById(1L);

	}

	@Test
	void testFindAllRoles() {
		List<Role> roles = new ArrayList<>();
		roles.add(new Role(1L, "ROLE_ADMIN"));
        when(roleRep.findAll()).thenReturn(roles);

        service.findAllRoles();

        verify(roleRep, times(1)).findAll();
	}

}
