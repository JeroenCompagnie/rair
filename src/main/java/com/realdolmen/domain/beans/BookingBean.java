package com.realdolmen.domain.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.realdolmen.domain.flight.Booking;
import com.realdolmen.repository.BookingRepository;

@Named("bookingBean")
@SessionScoped
public class BookingBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7742740715287006737L;
	
	@Inject
	private LoginBean loginBean;
	
	@EJB
	private BookingRepository bookingRepository;
	
	private String urlCode = "";
	
	private Booking booking;

	public String getUrlCode() {
		return urlCode;
	}

	public void setUrlCode(String urlCode) {
		this.urlCode = urlCode;
	}
	
	public boolean checkRightUser(){
		return false;
	}

	public Booking getBooking() {
		Booking bookingFound = bookingRepository.findById(urlCode);
		if(bookingFound == null){
			// TODO booking bestaat niet
			booking = null;
			return booking;
		}
		if(loginBean.getIsUserNull() || 
				bookingFound.getCustomer().getUserName() != loginBean.getUserName()){
			// TODO user niet ingelogd of niet juiste user
			booking = null;
			return booking;
		}
		booking = bookingFound;
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}
}
