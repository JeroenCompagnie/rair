package com.realdolmen.domain.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.validator.constraints.NotEmpty;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // TODO dit nog eens nakijken naar andere inheritancetypes
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2945389852701706104L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;

	@NotEmpty
	protected String firstName;
	
	@NotEmpty
	protected String lastName;
	
	@NotEmpty
	protected String hashedPassword;
	
	@NotEmpty
	@Column(unique=true)
	protected String userName;
	
	public User(){
		
	}
	
	public User(String firstName, String lastName, String unhashedPassword, String userName) {
//		super();
		this.firstName = firstName;
		this.lastName = lastName;
		setHashedPassword(unhashedPassword);
		this.userName = userName;
	}

	public long getId() {
		return id;
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
//TODO vragen of dit protected mag zijn... (Hoe anders testen?)
//	public String getHashedPassword() {
//		return hashedPassword;
//	}

	public void setHashedPassword(String unhashedPassword) {
		this.hashedPassword = BCrypt.hashpw(unhashedPassword, BCrypt.gensalt(12));
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Boolean checkPassword(String candidate){
		if (BCrypt.checkpw(candidate, hashedPassword)){
			System.out.println("It matches");
			return true;
		}
		else{
			System.out.println("It does not match");
			return false;
		}
	}

}
