package com.realdolmen.domain.flight;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.realdolmen.repository.EmployeeRepository;

public class EmployeeRepositoryTest {
	
	EmployeeRepository er;
	@Mock
	EntityManager entityManager;

	@Before
	public void init(){

	}

	@Test
	public void test() {
		
		if(er==null)
		{System.out.println("er = null");}
		er.findAll();
	}

}
