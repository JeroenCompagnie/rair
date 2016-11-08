package com.realdolmen.domain.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.flight.SeatType;
import com.realdolmen.domain.user.Partner;
import com.realdolmen.repository.FlightRepository;
import com.realdolmen.repository.PartnerRepository;

@Named("search")
@RequestScoped //////////////////////////////////IF DONT WORK TRY SESSIONSCOPE
public class SearchBean {

		private List<Flight> flights;
		private List<Flight> flights2;
		
		
		@PersistenceContext
		private EntityManager entityManager;
		
		@EJB
		private FlightRepository flightRepository;
		
		@EJB
		private PartnerRepository partnerRepository;
		
		@PostConstruct
		public void init()
		{
			//setFlights(new ArrayList<Flight>());
			//setFlights(entityManager.createQuery("select f from Flight f",Flight.class).getResultList());
			setFlights2(entityManager.createNamedQuery("Flight.findAll",Flight.class).getResultList());
			Partner partner = partnerRepository.findById(1001L);
			System.out.println("partnerName="+partner.getName());
			setFlights2(flightRepository.findByParams(SeatType.Economy, partner, new Date()));
		}


		public List<Flight> getFlights() {
			return flights;
		}


		public void setFlights(List<Flight> flights) {
			this.flights = flights;
		}


		public List<Flight> getFlights2() {
			return flights2;
		}


		public void setFlights2(List<Flight> flights2) {
			this.flights2 = flights2;
		}
}
