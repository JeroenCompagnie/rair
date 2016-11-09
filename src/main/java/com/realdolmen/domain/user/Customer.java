package com.realdolmen.domain.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.realdolmen.domain.flight.Booking;

@Entity
public class Customer extends User{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -289701417497673496L;
	
	@NotNull
	@OneToOne
	protected Address address;
	
	@NotEmpty
	@Column(unique=true)
	protected String email;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="customer")
	protected List<Booking> bookingsList = new ArrayList<>();
	
	public Customer(){
		
	}

	public Customer(Address address, String email, String firstName,
			String lastName, String password, String userName) {
		super(firstName, lastName, password, userName);
		this.address = address;
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Booking> getBookingsList() {
		return bookingsList;
	}

	public void setBookingsList(List<Booking> bookingsList) {
		this.bookingsList = bookingsList;
	}
	
	public void addBooking(Booking b){
		bookingsList.add(b);
	}
	
}
