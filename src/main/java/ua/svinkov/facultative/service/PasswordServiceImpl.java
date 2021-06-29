package ua.svinkov.facultative.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public PasswordServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public String encodePassword(String password) {
		return bCryptPasswordEncoder.encode(password);
	}

	@Override
	public boolean compareRawAndEncodedPassword(String raw, String encoded) {
		return bCryptPasswordEncoder.matches(raw, encoded);
	}
}
