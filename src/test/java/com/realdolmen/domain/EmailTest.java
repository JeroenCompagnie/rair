package com.realdolmen.domain;

import org.junit.Test;

import com.realdolmen.email.MailService;

public class EmailTest {
	@Test
	public void sendEmail(){
		MailService mailService = new MailService();
		mailService.mail("jeroen.compagnie@gmail.com", "Java test", "This was sent from java :3");
	}
}
