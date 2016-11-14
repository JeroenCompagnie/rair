package com.realdolmen.domain.flight;

import java.time.Duration;
import java.util.Date;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import com.realdolmen.course.utilities.persistence.JpaPersistenceTest;
import com.realdolmen.dateConverter.DateConverter;
import com.realdolmen.domain.user.Partner;

public class DiscountTest extends JpaPersistenceTest{
	Airport airport;
	Partner partner;
	Flight flight;
	EntityManager em;
	
	@Before
	public void init(){
		em = entityManager();
		
		airport = new Airport("city", "country", "countryCode", "airportName", "internationalAirportCode");
		em.persist(airport);
		partner = new Partner("partnerName", "firstName", "lastName", "userName", "password");
		em.persist(partner);
		flight = new Flight(partner, airport, airport, new Date(), Duration.ofMinutes(60));
		em.persist(flight);
	}
	
	@Test
	public void testBasicPercentageDiscount(){
		Discount discountPercentage = new Discount(true, true, 0.05, false, null, null);
		Double baseprice = 100.0;
		
		Double discountedPrice = discountPercentage.addDiscountToPrice(baseprice);
		
		assertEquals(95.0,discountedPrice, 0.0001);
	}
	
	@Test
	public void testBasicRealvalueDiscount(){
		Discount discountRealvalue = new Discount(true, false, 9.99, false, null, null);
		Double baseprice = 100.0;
		
		Double discountedPrice = discountRealvalue.addDiscountToPrice(baseprice);
		
		assertEquals(90.01,discountedPrice, 0.0001);
	}
	
	@Test
	public void testBasicPercentageDiscountInPeriodFuture(){
		
		Date begin= DateConverter.getDateXDaysAfterToday(5);
		Date end= DateConverter.getDateXDaysAfterToday(7);
		
		Discount discountPercentage = new Discount(true, true, 0.05, true, begin, end);
		Double baseprice = 100.0;
		
		Double discountedPrice = discountPercentage.addDiscountToPrice(baseprice);
		
		assertEquals(100.0,discountedPrice, 0.0001);
	}
	
	@Test
	public void testBasicPercentageDiscountInPeriodPast(){
		Date begin= DateConverter.getDateXDaysBeforeToday(7);
		Date end = DateConverter.getDateXDaysBeforeToday(5);
		
		Discount discountPercentage = new Discount(true, true, 0.05, true, begin, end);
		Double baseprice = 100.0;
		
		Double discountedPrice = discountPercentage.addDiscountToPrice(baseprice);
		
		assertEquals(100.0,discountedPrice, 0.0001);
	}
	
	@Test
	public void testBasicPercentageDiscountInRightPeriod(){
		Date begin= DateConverter.getDateXDaysBeforeToday(1);
		Date end = DateConverter.getDateXDaysAfterToday(1);
		
		Discount discountPercentage = new Discount(true, true, 0.05, true, begin, end);
		Double baseprice = 100.0;
		
		Double discountedPrice = discountPercentage.addDiscountToPrice(baseprice);
		
		assertEquals(95.0,discountedPrice, 0.0001);
	}
	
	@Test
	public void testFlightWithStandardDiscount(){
		assertEquals("partnerName", flight.getPartner().getName());
		assertEquals(110.0, flight.applyDiscountsToPrice(100.0),0.0001);
	}
	
	@Test
	public void testFlightWithAFewDiscounts(){
		flight.addDiscount(new Discount(true, true, 0.01));
		flight.addDiscount(new Discount(false, true, 0.01));
		flight.addDiscount(new Discount(true, false, 1.0));
		flight.addDiscount(new Discount(false, false, 1.0));
		em.merge(flight);
		double expectedSelf = 100.0 + 100.0*0.1;
		expectedSelf = expectedSelf -expectedSelf*0.01;
		expectedSelf = expectedSelf -expectedSelf*0.01;
		expectedSelf = expectedSelf -1;
		expectedSelf = expectedSelf -1;
		
		double expected = 100.0;
		expected = new Discount(true, true, -0.1).addDiscountToPrice(expected);
		System.err.println(expected);
		expected = new Discount(true, true, 0.01).addDiscountToPrice(expected);
		System.err.println(expected);
		expected = new Discount(true, true, 0.01).addDiscountToPrice(expected);
		System.err.println(expected);
		expected = new Discount(true, false, 1.0).addDiscountToPrice(expected);
		System.err.println(expected);
		expected = new Discount(true, false, 1.0).addDiscountToPrice(expected);
		System.err.println(expected);
		System.err.println("-----");
		
		assertEquals(expectedSelf, expected, 0.0001);
		assertEquals(expected, flight.applyDiscountsToPrice(100.0),0.0001);
	}
}


