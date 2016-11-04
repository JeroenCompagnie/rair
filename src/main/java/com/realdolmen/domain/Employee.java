package com.realdolmen.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Employee extends User{
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	public Employee(){
		
	}
}
