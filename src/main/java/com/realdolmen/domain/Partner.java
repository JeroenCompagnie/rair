package com.realdolmen.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Partner extends User{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4558912184599597151L;
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	protected String name;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="partner")
	protected List<Flight> flightList = new ArrayList<Flight>();
	
	public Partner(){
		
	}
	
	public Partner(String partnerName, String firstName, String lastName,
			String userName, String password){
		super(firstName,lastName,password,userName);
		this.name= partnerName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}
	
	public void addFlight(Flight f){
		flightList.add(f);
	}
}
