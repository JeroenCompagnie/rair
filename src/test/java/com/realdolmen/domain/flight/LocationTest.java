package com.realdolmen.domain.flight;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LocationTest {
	Location l;
	@Before
	public void init(){
		l= new Location("airportName","airportCountry","airportInternationalCode",GlobalRegion.EasternEurope);
	}

	@Test
	public void locationUnitTest() {
		assertEquals("airportName",l.getAirportName());
		assertEquals("airportCountry",l.getAirportCountry());
		assertEquals("airportInternationalCode",l.getAirportInternationalCode());
		assertEquals(GlobalRegion.EasternEurope,l.getGlobalRegion());
	}

}
