package ua.svinkov.facultative.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * User entity
 * 
 * @author R.Svinkov
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
@Table
public class User implements Serializable, UserDetails {

	private static final long serialVersionUID = 3029863271984170488L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userid;

	@Column(nullable = false)
	private String login;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String surname;

	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	private Role role;

	@Column(nullable = false, columnDefinition = "tinyint DEFAULT '1'")
	private boolean status;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(role);
	}

	@Override
	public String getUsername() {
		return login;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isStatus();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return isStatus();
	}
}
