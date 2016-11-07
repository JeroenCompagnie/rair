package com.realdolmen.domain.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import javax.inject.Named;

import com.realdolmen.domain.flight.Flight;

@Named("search")
@RequestScoped //////////////////////////////////IF DONT WORK TRY SESSIONSCOPE
public class SearchBean {

		private List<Flight> flights;
		
		@PostConstruct
		public void init()
		{
			flights = new ArrayList<Flight>();
		}
}
