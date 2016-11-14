package com.realdolmen.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

import com.realdolmen.domain.flight.Discount;

public class HashSetTest {

	@Test
	public void testMethodListWithoutNullOrDuplicates(){
		Discount d1 = new Discount();
		Discount d2 = new Discount();
		Discount d3 = new Discount();
		Discount d4 = new Discount();
		ArrayList<Discount> list = new ArrayList<>();
		for(int i = 0; i < 3; i++){
			list.add(d1);
			list.add(d2);
			list.add(d3);
			list.add(d4);
			list.add(null);
		}
		
		assertTrue(list.contains(null));
		list=getListWithoutNullOrDuplicates(list);
		assertEquals(4, list.size());
		
	}
	
	@Test
	public void testMethodWithEmptyList(){
		ArrayList<Discount> list = new ArrayList<>();
		list=getListWithoutNullOrDuplicates(list);
		assertEquals(0, list.size());
	}
	
	public ArrayList<Discount> getListWithoutNullOrDuplicates(ArrayList<Discount> discounts){
		HashSet<Discount> hs = new HashSet<Discount>();
		hs.addAll(discounts);
		discounts.clear();
		discounts.addAll(hs);
		while(discounts.contains(null)){
			discounts.remove(null);
		}
		return discounts;
	}
}

