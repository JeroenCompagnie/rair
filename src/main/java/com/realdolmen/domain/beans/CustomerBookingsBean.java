package com.realdolmen.domain.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.realdolmen.domain.flight.Booking;
import com.realdolmen.repository.CustomerRepository;

@Named("customerBookingBean")
@SessionScoped
public class CustomerBookingsBean implements Serializable{
	
	@Inject 
	CustomerRepository customerRepository;
	
	@Inject
	LoginBean loginBean;

	/**
	 * 
	 */
	private static final long serialVersionUID = -8668542426177875689L;
	
	public ArrayList<Booking> getBookingsList(){
		return customerRepository.getAllBookingsBy(loginBean.getUser());
	}

}
