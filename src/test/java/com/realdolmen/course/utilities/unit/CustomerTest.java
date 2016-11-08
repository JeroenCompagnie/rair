package com.realdolmen.course.utilities.unit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.realdolmen.domain.user.Address;
import com.realdolmen.domain.user.Customer;

public class CustomerTest {

	Customer c;
	Address a;
	@Before
	public void init()
	{
		a = new Address("teststreet",1,3670,"Belgium");
		c = new Customer(a,"email@address.com","firstName","lastName","password","userName");
	}
	@Test
	public void customerUnitTest() {
		assertEquals(a,c.getAddress());//Test address
		assertEquals("email@address.com",c.getEmail());//Test Customer
		assertEquals("firstName",c.getFirstName());
		assertEquals("lastName",c.getLastName());
		assertEquals("password",c.getHashedPassword());
		assertEquals("userName",c.getUserName());
		
		
	}

}
