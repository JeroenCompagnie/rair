package com.realdolmen.security;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 
 * @author JCPBB69
 *	
 * Class only used to test the hashpw and checkpw of BCrypt
 */
public class PasswordHashSystem {

	public PasswordHashSystem(){

	}

	public String hashPassword(String unhashedPassword){
		// Hash a password for the first time
		String hashed = BCrypt.hashpw(unhashedPassword, BCrypt.gensalt(12));
		return hashed;
	}

	public String hashPasswordStrongSalt(String unhashedPassword){
		// gensalt's log_rounds parameter determines the complexity
		// the work factor is 2**log_rounds, and the default is 10
		String hashed = BCrypt.hashpw(unhashedPassword, BCrypt.gensalt(14));
		return hashed;
	}
	
	public boolean checkPassword(String candidate, String hashed){
		// Check that an unencrypted password matches one that has
		// previously been hashed
		if (BCrypt.checkpw(candidate, hashed)){
			System.out.println("It matches");
			return true;
		}
		else{
			System.out.println("It does not match");
			return false;
		}
	}
}
