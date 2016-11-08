package com.realdolmen.domain.flight;

import java.security.SecureRandom;
import java.math.BigInteger;

public final class RandomStringGenerator {
	
	private static SecureRandom random = new SecureRandom();
	
	public static String getRandomString(){
		return new BigInteger(130, random).toString(32);
	}
}
