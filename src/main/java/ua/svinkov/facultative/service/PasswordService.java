package ua.svinkov.facultative.service;

public interface PasswordService {

	String encodePassword(String password);

	boolean compareRawAndEncodedPassword(String raw, String encoded);
}
