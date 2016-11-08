package com.realdolmen.course.utilities.unit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.user.Partner;

public class PartnerTest {

	Partner p;
	@Before
	public void init(){
		p = new Partner("partnerName","firstName","lastName","userName","password");
		for(int i=1;i<=5;i++)
		{
			Flight p = new Flight();
		}
	}

	@Test
	public void partnerUnitTest() {
		assertEquals("partnerName",p.getName());
		assertEquals("firstName",p.getFirstName());
		assertEquals("lastName",p.getLastName());
		assertEquals("userName",p.getUserName());
	//	assertEquals("password",p.getHashedPassword());
	}
	
	@Test
	public void addFlightsTest(){
		
	}

}
