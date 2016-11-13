package com.realdolmen.domain.flight;

import static org.junit.Assert.*;

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
		
		assertEquals(95.0,discountedPrice, 0.0001);
	}
}
