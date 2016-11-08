package com.realdolmen.domain.beans;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("bookingBean")
@SessionScoped
public class BookingBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7742740715287006737L;
	
	private String urlCode = "";

	public String getUrlCode() {
		return urlCode;
	}

	public void setUrlCode(String urlCode) {
		this.urlCode = urlCode;
	}
	
	public boolean checkRightUser(){
		return false;
	}
}
