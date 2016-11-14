package com.realdolmen.domain.flight;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.realdolmen.domain.flight.Booking;
import com.realdolmen.domain.flight.PaymentStatus;
import com.realdolmen.domain.user.Address;
import com.realdolmen.domain.user.Customer;
import com.realdolmen.domain.user.Employee;
import com.realdolmen.domain.user.User;

public class BookingTest {
	
	Booking b;
	Address a;
	User c;
	Date d;
	//Employee e;
	@Before
	public void init()
	{
		//b = new Booking(PaymentStatus.PENDING, c, Calendar.getInstance());
		//a = new Address("teststreet",1,3670,"Hasselt","Belgium");
		c = new User("firstName","lastName","password","userName");
		b  = new Booking(PaymentStatus.PENDING,c);
		//e = new Employee("firstName","lastName","password","username");
	}

	@Test
	public void bookingUnitTest() {
		assertEquals(PaymentStatus.PENDING,b.getPaymentStatus());
		System.out.println("PaymentStatus = " +PaymentStatus.PENDING.toString());
		//assertEquals(a,b.getCustomer().getAddress());//Test address
		assertEquals(c,b.getCustomer());//Test Customer
		assertEquals(d,b.getDateOfReservation());//Test reservationDate
		assertNotEquals(b,new Booking(PaymentStatus.PENDING,c));//Test Id
		
		
		
	}

}
