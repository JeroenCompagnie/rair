package com.realdolmen.domain.flight;

import java.util.List;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.realdolmen.domain.user.Partner;

@StaticMetamodel(Flight.class)
public class Flight_ {
	public static volatile SingularAttribute<Flight, Integer> id;
    public static volatile SingularAttribute<Flight, Partner> partner;
    public static volatile SetAttribute<Flight, List<Seat>> seatList;
    public static volatile SingularAttribute<Flight, BookingOfFlight> bookingOfFlightList;
    public static volatile SingularAttribute<Flight, Location> destinationLocation;
    public static volatile SingularAttribute<Flight, Location> departureLocation;

}
