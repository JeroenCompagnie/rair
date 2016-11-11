package com.realdolmen.domain.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.realdolmen.domain.flight.Flight;
@NamedQueries(
@NamedQuery(name=Partner.findAll,query="SELECT p FROM Partner p"))
@Entity
public class Partner extends User{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4558912184599597151L;
	public static final String findAll = "Partner.findAll";
	
	protected String name;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="partner", fetch = FetchType.EAGER)
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
	
	public void addFlight(Flight f){
		flightList.add(f);
	}
	
	@Override
	public String toString()
	{
		return this.getUserName();
	}
}
