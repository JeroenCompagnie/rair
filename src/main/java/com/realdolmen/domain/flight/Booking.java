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
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.realdolmen.domain.user.Customer;
import com.realdolmen.domain.user.User;

@Entity
public class Booking implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4490556795691845585L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	@GenericGenerator(name = "uuid", strategy = "uuid2")
//	@GeneratedValue(generator = "uuid")
//	private String id;
	
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;
	
	@ManyToOne
	@JoinColumn(name="customer_id", nullable=false)
	private User customer;
	
	private Date dateOfReservation;

	//TODO: cascadetype nakijken!!
	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
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
	
	public Booking(PaymentStatus paymentStatus, User customer) {
		this.paymentStatus = paymentStatus;
		this.customer = customer;
		this.dateOfReservation = new Date();
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public User getCustomer() {
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
	
	public String printBooking()
	{
		String bookingOverview;
		String sub = null;
		Double totPrice=0.0;
		String seatType = this.bookingOfFlightList.get(0).getSeat().getType().toString();
		String airline = this.bookingOfFlightList.get(0).getFlight().getPartner().getUserName();
		int count=0;
		for (BookingOfFlight bof :this.bookingOfFlightList)
		{
			totPrice+=bof.getPrice();
			count++;
		}
		bookingOverview="<html><body>Dear " + this.getCustomer().getFirstName() + " " + this.getCustomer().getLastName() +"<br/>";
		bookingOverview+="<p>You booked <b>"+count+"</b> seats with airline <b>" + airline +"</b></p>";
		bookingOverview+= "<p>The price for <b>"+count+"</b> seats was <b>" + totPrice+"</b></p>"; 
		bookingOverview+="</body></html>";
		return bookingOverview;
	}

	public Long getId() {
		return id;
	}
	
	public void addBookingOfFlight(BookingOfFlight bf){
		bf.setNumberInBooking(bookingOfFlightList.size()+1);
		bookingOfFlightList.add(bf);
	}
	
	public void getQrCode(){
		QRCode.createQrCodeForInvoice("" + id);
	}
	
}
