package com.realdolmen.course.utilities.unit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.realdolmen.domain.user.Address;



public class AddressTest {

	Address a;
	@Before
	public void init() {
		a = new Address("teststreet",1,3670,"Belgium");
	}

	@Test
	public void addressUnitTest() {
		assertEquals("teststreet",a.getStreet());
		assertEquals(1,a.getNumber());
		assertEquals(3670,a.getPostalCode());
		assertEquals("Belgium",a.getPostalCode());
	}

}
