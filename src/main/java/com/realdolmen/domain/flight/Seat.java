package com.realdolmen.domain.flight;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Seat implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4783778236554830477L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private SeatType type;
	
	private double basePrice;
	
	public Seat(){
		
	}

	public Seat(SeatType type, double basePrice) {
		super();
		this.type = type;
		this.basePrice = basePrice;
	}

	public SeatType getType() {
		return type;
	}

	public void setType(SeatType type) {
		this.type = type;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public Long getId() {
		return id;
	}	
}
