package com.realdolmen.domain.flight;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AirportTest {
	Airport a;
	@Before
	public void init(){
		a = new Airport("city1", "country1", "cc1", "airportname1", "421","globalregion1");
	}

	@Test
	public void locationUnitTest() {
		assertEquals("airportname1",a.getAirportName());
		assertEquals("country1",a.getCountry());
		assertEquals("421",a.getId());
		assertEquals("globalregion1",a.getGlobalRegion());
	}

}
