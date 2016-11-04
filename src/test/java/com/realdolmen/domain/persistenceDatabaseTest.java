package com.realdolmen.domain;

import javax.persistence.EntityManager;

import org.apache.poi.hssf.record.chart.AxisUsedRecord;
import org.junit.Test;

import com.realdolmen.course.utilities.persistence.JpaPersistenceTest;

public class persistenceDatabaseTest extends JpaPersistenceTest{
	
	@Test
	public void initPersistTest() {
		EntityManager em = entityManager();
		Address a = new Address("DummyStraat", 42, 9999, "Belgium");
		//Address a persisten zodat het een ID heeft
		em.persist(a);
		assertNotNull(a.getId());
		Customer c = new Customer(a, "jon.snow@gmail.com", 
				"jon", "snow", "iknownothing", "KingOfTheNorth");
		assertNotNull(c);
		assertEquals("DummyStraat", c.getAddress().getStreet());
		em.persist(c.getId());
		
		
	}
}
