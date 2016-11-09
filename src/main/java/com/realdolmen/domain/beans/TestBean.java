package com.realdolmen.domain.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.realdolmen.domain.user.Employee;
import com.realdolmen.repository.EmployeeRepository;

@Named("test")
@RequestScoped
public class TestBean {
	
		@EJB
		EmployeeRepository er;
		
		public List<Employee> getAllEmploye()
		{
			return er.findAll();
		}

}
