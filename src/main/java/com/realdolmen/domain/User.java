package com.realdolmen.domain;

import java.io.Serializable;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.hibernate.validator.constraints.NotEmpty;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // TODO dit nog eens nakijken naar andere inheritancetypes
public class User implements Serializable{

	
	@NotEmpty
	protected String firstName;
	
	@NotEmpty
	protected String lastName;
	
	@NotEmpty
	protected String hashedPassword; //TODO: hashing toevoegen
	
	@NotEmpty
	protected String userName;
	
	public User(){
		
	}
	
	public User(String firstName, String lastName, String hashedPassword, String userName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.hashedPassword = hashedPassword;
		this.userName = userName;
	}



	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	

}
