package com.realdolmen.domain.flight;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.realdolmen.domain.user.Customer;

@Entity
public class Booking implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4490556795691845585L;

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;
	
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;
	
	@ManyToOne
	@JoinColumn(name="customer_id", nullable=false)
	private Customer customer;
	
	private Date dateOfReservation;

	//TODO: cascadetype nakijken!!
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(
			joinColumns = @JoinColumn(table = "booking",
		            name="booking_id",
		            referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(table = "bookingOfFlight",
            name="bookingOfFlight_id",
            referencedColumnName = "id")
	)
	private List<BookingOfFlight> bookingOfFlightList = new ArrayList<>();
	
	public Booking(){
		
	}
	
	public Booking(PaymentStatus paymentStatus, Customer customer, Date dateOfReservation) {
		this.paymentStatus = paymentStatus;
		this.customer = customer;
		this.dateOfReservation = dateOfReservation;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Date getDateOfReservation() {
		return dateOfReservation;
	}

	public void setDateOfReservation(Date dateOfReservation) {
		this.dateOfReservation = dateOfReservation;
	}

	public List<BookingOfFlight> getBookingOfFlightList() {
		return bookingOfFlightList;
	}

	public void setBookingOfFlightList(List<BookingOfFlight> bookingOfFlightList) {
		this.bookingOfFlightList = bookingOfFlightList;
	}

	public String getId() {
		return id;
	}
	
	public void addBookingOfFlight(BookingOfFlight bf){
		bf.setNumberInBooking(bookingOfFlightList.size()+1);
		bookingOfFlightList.add(bf);
	}
	
	public void getQrCode(){
		QRCode.createQrCodeForInvoice(id);
	}
	
}
