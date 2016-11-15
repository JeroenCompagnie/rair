package com.realdolmen.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.realdolmen.domain.flight.Discount;
import com.realdolmen.domain.flight.Airport;
import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.flight.Seat;
import com.realdolmen.domain.flight.SeatType;
import com.realdolmen.domain.user.Employee;
import com.realdolmen.domain.user.Partner;
import com.realdolmen.domain.user.User;

@Stateless
public class FlightRepository {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	EntityManager em;

	public Flight save(Flight flight) {
		em.persist(flight);
		return flight;
	}
	
	public Flight update(Flight flight)
	{
		em.merge(flight);
		return flight;
	}

	public Flight create(Flight flight) {
		em.persist(flight);
		return flight;
	}

	public Flight findById(long id) {
		return em.find(Flight.class, id);
	}

	public List<Flight> findAll() {
		return em.createNamedQuery("Flight.findAll", Flight.class).getResultList();
	}

	public void remove(Long flightId) {
		logger.info("Removing user with id " + flightId);
		em.remove(em.getReference(User.class, flightId));
	}

	public List<Flight> findByParams2(SeatType t, Partner p, Date d) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);
		Root<Flight> from = cq.from(Flight.class);
		From<Flight, Seat> join = from.join("seatList");
		Path<Seat> s = join.get("type");
		Predicate predicate = cb.equal(s, t);
		cq.where(predicate);
		cq.select(from);
		TypedQuery<Flight> typedQuery = em.createQuery(cq);

		return typedQuery.getResultList();
	}


	/*public List<Flight> findByParams3(SeatType t, Partner p, Date d) {
//works but has duplicates
>>>>>>> 4014ef9764278ee95b737d9b07b8ffddcc42a17a
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);
		Root<Flight> from = cq.from(Flight.class);
		From<Flight, Seat> join = from.join("seatList");
		Path<Seat> s = join.get("type");
		Predicate predicate = cb.equal(s, t);
		cq.where(predicate);
		cq.select(from).distinct(true);
		TypedQuery<Flight> typedQuery = em.createQuery(cq);

		return typedQuery.getResultList();
	}*/

	public List<Flight> findByParams(SeatType t, Partner partner, Date departureDate,Airport destination,Airport departure,String globalRegion,int numberOfSeats) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);
		Root<Flight> from = cq.from(Flight.class);
		Date date2;
		Calendar c = Calendar.getInstance();
		c.setTime(departureDate);
		c.add(Calendar.DATE, 1); 
		date2 = c.getTime();
		Calendar c2 = Calendar.getInstance();
		c2.setTime(departureDate);
		c2.add(Calendar.DAY_OF_YEAR, -1);
		c2.add(Calendar.MINUTE, 719);
		departureDate=c2.getTime();
		//departureDate=null;
		//t=null;
		//partner=null;
		//destination=null;
		//departure=null;
		//globalRegion=null;
		//System.out.println("SeatType: " + t.toString()+ " from findByParams");
		//System.out.println("Partner: " + partner.toString()+ " from findByParams");
		//System.out.println("DepartureDate: " + departureDate.toString()+ " from findByParams");
		//System.out.println("Destination: " + destination.toString()+ " from findByParams");
		//System.out.println("Departure: " + departure.toString()+ " from findByParams");
		//System.out.println("GlobalRegion: " + globalRegion.toString()+ " from findByParams");
		
		List<Predicate> predicates = new ArrayList<Predicate>();

		// Adding predicates in case of parameter not being null
		if (t != null) {
			System.out.println("SeatType tested " + t);
			From<Flight, Seat> join = from.join("seatList");
			Path<Seat> s = join.get("type")	;
			predicates.add(cb.equal(s, t));
			//System.out.println(s + " seattype from database");
		}
		if (partner != null) {
			System.out.println("Partner tested " + partner);
			Path<Partner> dbPartner=from.<Partner>get("partner");
			predicates.add(cb.equal(dbPartner, partner));
			//System.out.println(dbPartner + " partner from database");
		}
		if (departureDate != null) {
			System.out.println("departureDate tested " +departureDate);
			Path<Date> dbDepartureDate=from.<Date>get("dateOfDeparture");
			predicates.add(cb.between(dbDepartureDate, departureDate, date2));
			//System.out.println(dbDepartureDate + " departuredate from database");
		}
		if (destination != null)
		{
			System.out.println("destination tested " +destination);
			Path<Airport> dbAirport = from.<Airport>get("destinationAirport");
			predicates.add(cb.equal(dbAirport, destination));
			//System.out.println(dbAirport + " destinationAirports from database");
		}
		if (departure != null)
		{
			System.out.println("departure tested " + departure);
			Path<Airport> dbAirport = from.<Airport>get("departureAirport");
			//System.out.println(dbAirport + " destination_airport");
			predicates.add(cb.equal(dbAirport, departure));
		}
		if (globalRegion != null)
		{
			System.out.println("GlobalRegion tested " + globalRegion);
			Path<Airport> dbAirport = from.<Airport>get("destinationAirport");
			Path<String> dbGlobalRegion = dbAirport.<String>get("globalRegion");
			//System.out.println(dbAirport + " dbAirport");
			//System.out.print(dbGlobalRegion) + " = dbGlobalRegion");
			//System.out.println(globalRegion.toString()+ " = globalRegion");
			//System.out.println(cq.getSelection());
			predicates.add(cb.equal(dbGlobalRegion, globalRegion));
		}
		cq.select(from).where(predicates.toArray(new Predicate[] {}));
		//cq.select(from).where(predicates.toArray(new Predicate[] {})).distinct(true);
		//cq.having(cb.in(from.get("seatList")))
		return em.createQuery(cq).getResultList();
	}

	public ArrayList<Flight> getFlightsByPartner(Partner partner) {
		ArrayList<Flight> resultList = new ArrayList<Flight>();
		try{
			resultList = (ArrayList<Flight>) em.createQuery("select f from Flight f where f.partner = :arg", Flight.class)
					.setParameter("arg", partner).getResultList();
		}
		catch (Exception e){
			logger.error("No flights found for partner: " + partner.getName());
			return resultList;
		}
		return (ArrayList<Flight>) resultList;
	}

	public Flight getFlightByPartner(Partner partner, Long partnerFlightId) {
		Flight f = null;
		try{
			f = em.createQuery("select f from Flight f where f.partner = :arg1 and f.id = :arg2", Flight.class)
					.setParameter("arg1", partner)
					.setParameter("arg2", partnerFlightId)
					.getSingleResult();
		}
		catch (Exception e){
			logger.error("No flight found for partner: " + partner.getName() +" with flight id: " + partnerFlightId);
			return f;
		}
		return f;
	}

	public void setSeatPrice(Partner partner, Long partnerFlightId, SeatType seatType, double newPrice) {
		Flight find = em.find(Flight.class, partnerFlightId);
		if(find.getPartner().getId() == partner.getId()){
			find.setSeatBasePrice(newPrice, seatType);
			em.merge(find);
		}
	}

	public int getNumberOfSeatsBooked(Flight flight, List<SeatType> seatTypes){
		if(flight == null){
			System.err.println("flight was null");
			return -41;
		}
		return (int)(long) em.createQuery(
				"select count(s) "
						+ "from BookingOfFlight bof join bof.seat s "
						+ "where bof.flight = :arg1 "
						+ "and bof.seat = s "
						+ "and s.type IN :arg2")
				.setParameter("arg1", flight)
				.setParameter("arg2", seatTypes)
				.getSingleResult();

	}

	public int getNumberOfSeatsLeft(Flight flight, List<SeatType> seatTypes){
		if(flight == null){
			System.err.println("flight was null");
			return -41;
		}
		return (int)(long) em.createQuery(
				"select count(f) "
						+ "from Flight f join "
						+ "f.seatList s WHERE f.id = :flightId "
						+ "and s.type IN :seatType")
				.setParameter("flightId", flight.getId())
				.setParameter("seatType", seatTypes)
				.getSingleResult();
	}

	public boolean changeDefaultPriceCharge(User user, Discount d, Flight f) {
		if(user.getClass() == Employee.class){
			f.setDefaultPriceCharge(user, d);
			em.merge(f);
			return true;
		}
		return false;
	}

	
	public Flight removeDiscount(User user, long id, Flight flight) {
		if(user.getClass() == Employee.class || user.getClass()==Partner.class){
			Discount d = em.find(Discount.class, id);

			if(d != null && ((d.isByEmployee() && user.getClass() == Employee.class) ||
					!d.isByEmployee() && user.getClass()==Partner.class)){
				
				try{
					System.err.println("FLIGHT REPOSITORY: begin of try");
					flight.removeDiscount(d);
					System.err.println("FLIGHT REPOSITORY: removed discount with id: " + d.getId()+ " from flight " + flight.getId());
					System.err.println("Flight contains discount " +flight.getDiscountsList().contains(d));
					Flight returnFlight = em.merge(flight);
					em.remove(d);
					em.flush();
					System.err.println("Discount in db: " + em.find(Discount.class, id)!=null);
					return returnFlight;
				}
				catch(Exception e){
					System.err.println("FAILED TO REMOVE DISCOUNT, stacktrace: "+e.getMessage() + " short description  " + e.toString());
				}
			}
		}
		return flight;
	}

	public void addDiscount(Discount d, Flight f) {
		f.addDiscount(d);
		em.merge(f);
			System.err.println("Id of discount " + d.getId());
		
	}
}
