package ua.svinkov.facultative.service;

/**
 * Interface for manage passwords
 * 
 * @author R. Svinkov
 *
 */
public interface PasswordService {

	/**
	 * Encoding of password
	 * 
	 * @param password Password
	 * @return Encoded password
	 */
	String encodePassword(String password);

	/**
	 * Compare password inputed by user and password in DB
	 * 
	 * @param raw     Password without encoding
	 * @param encoded Encoded password
	 * @return Result of compare
	 */
	boolean compareRawAndEncodedPassword(String raw, String encoded);
}
