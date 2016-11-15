package com.realdolmen.domain;

import java.util.Date;

import org.junit.Test;

import com.realdolmen.email.Email;

public class EmailTest {
	@Test
	public void sendEmail(){
		Email email = new Email();
		String[] recipients = {"jeroen.compagnie@gmail.com"};
		email.sendMailStandardSender(recipients, "Java test", "This was sent from java :3 at " + new Date().toString());
	}
}
