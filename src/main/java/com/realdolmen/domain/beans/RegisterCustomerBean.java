package com.realdolmen.domain.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.realdolmen.domain.user.Address;
import com.realdolmen.domain.user.Customer;
import com.realdolmen.repository.UserRepository;

//@Named("registerCustomerBean")
@ManagedBean
@SessionScoped
public class RegisterCustomerBean implements Serializable{
	@EJB
    UserRepository userRepository;
	
	@NotEmpty
	@Size(min = 1, max = 50, message = "{firstNameError}")
	private String firstName;
	
	@NotEmpty
	@Size(min = 1, max = 50, message = "{lastNameError}")
	private String lastName;
	
	@NotEmpty
	@Size(min = 4, max = 50, message = "{userNameError}")
	private String userName;
	
	@NotEmpty
	@Size(min=8, max=30, message="{unhashedPasswordError")
	private String unhashedPassword;
	
	@Email
	private String email;
	
	@NotNull
	private Address address = new Address();

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUnhashedPassword() {
		return unhashedPassword;
	}

	public void setUnhashedPassword(String unhashedPassword) {
		this.unhashedPassword = unhashedPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public String register(){
		System.err.println("TEST123: " + firstName);
		Customer c = new Customer(address, email, firstName,
				lastName, unhashedPassword, userName);
		Customer c2 = userRepository.saveCustomer(c);
		address = new Address();
		System.err.println(c2.getId());
		
		return "registrationSuccessful.xhtml";
	}
}
