package com.realdolmen.domain.flight;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class DiscountTest {
	
	@Test
	public void testBasicPercentageDiscount(){
		Discount discountPercentage = new Discount(true, 0.05, false, null, null);
		Double baseprice = 100.0;
		
		Double discountedPrice = discountPercentage.addDiscountToPrice(baseprice);
		
		assertEquals(95.0,discountedPrice, 0.0001);
	}
	
	@Test
	public void testBasicRealvalueDiscount(){
		Discount discountRealvalue = new Discount(false, 9.99, false, null, null);
		Double baseprice = 100.0;
		
		Double discountedPrice = discountRealvalue.addDiscountToPrice(baseprice);
		
		assertEquals(90.01,discountedPrice, 0.0001);
	}
	
	@Test
	public void testBasicPercentageDiscountInPeriodFuture(){
		Date begin = new Date(2016,12,1);
		Date end = new Date(2016,12,2);
		Discount discountPercentage = new Discount(true, 0.05, true, begin, end);
		Double baseprice = 100.0;
		
		Double discountedPrice = discountPercentage.addDiscountToPrice(baseprice);
		
		assertEquals(100.0,discountedPrice, 0.0001);
	}
	
	@Test
	public void testBasicPercentageDiscountInPeriodPast(){
		Date begin = new Date(2016,11,1);
		Date end = new Date(2016,11,2);
		Discount discountPercentage = new Discount(true, 0.05, true, begin, end);
		Double baseprice = 100.0;
		
		Double discountedPrice = discountPercentage.addDiscountToPrice(baseprice);
		
		assertEquals(100.0,discountedPrice, 0.0001);
	}
	
	public void testBasicPercentageDiscountInRightPeriod(){
		Date begin = new Date(2016,11,1);
		Date end = new Date(2016,12,1);
		Discount discountPercentage = new Discount(true, 0.05, true, begin, end);
		Double baseprice = 100.0;
		
		Double discountedPrice = discountPercentage.addDiscountToPrice(baseprice);
		
		assertEquals(95.0,discountedPrice, 0.0001);
	}
}
