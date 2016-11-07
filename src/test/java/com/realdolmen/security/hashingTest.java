package com.realdolmen.security;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class hashingTest {
	
	@Test
	public void hashPasswordLenghtValidation(){
		PasswordHashSystem pws = new PasswordHashSystem();
		ArrayList<Integer> lengths = new ArrayList<>();
		for(int i = 0; i < 30; i++){
			String baseString = "baseString";
			int l = pws.hashPassword(getXTimesString(i+1,baseString)).length();
			lengths.add(l);
			assertEquals(60, l);
		}
	}

	private String getXTimesString(int x, String baseString) {
		String result = baseString;
		for(int i =0; i < x; i++){
			result += baseString;
		}
		return result;
	}
	
	@Test
	public void hashPasswordValidation(){
		String password = "myPassword123";
		PasswordHashSystem pws = new PasswordHashSystem();
		String hashedPassword = pws.hashPassword(password);
		
		assertTrue(pws.checkPassword(password, hashedPassword));
		assertFalse(pws.checkPassword("notTheRightPass", hashedPassword));
	}
}
