package com.realdolmen.domain.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Employee extends User{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6798024266517613650L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	public Employee(){
		
	}
	
	public Employee(String firstName,String lastName,String password,String userName)
	{
		super(firstName,lastName,password,userName);
	}
	
}
