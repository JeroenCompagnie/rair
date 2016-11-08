package com.realdolmen.domain.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.realdolmen.domain.flight.Flight;

@Named("search")
@RequestScoped //////////////////////////////////IF DONT WORK TRY SESSIONSCOPE
public class SearchBean {

		private List<Flight> flights;
		
		
		@PersistenceContext
		private EntityManager entityManager;
		
		
		@PostConstruct
		public void init()
		{
			setFlights(new ArrayList<Flight>());
			setFlights(entityManager.createQuery("select f from Flight f",Flight.class).getResultList());	
		}


		public List<Flight> getFlights() {
			return flights;
		}


		public void setFlights(List<Flight> flights) {
			this.flights = flights;
		}
}
