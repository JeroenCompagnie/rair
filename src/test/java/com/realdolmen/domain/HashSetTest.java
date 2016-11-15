package com.realdolmen.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

import com.realdolmen.domain.flight.DiscountPercentage;
import com.realdolmen.domain.flight.DiscountSuper;

public class HashSetTest {

	@Test
	public void testMethodListWithoutNullOrDuplicates(){
		DiscountSuper d1 = new DiscountPercentage();
		DiscountSuper d2 = new DiscountPercentage();
		DiscountSuper d3 = new DiscountPercentage();
		DiscountSuper d4 = new DiscountPercentage();
		ArrayList<DiscountSuper> list = new ArrayList<>();
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
		ArrayList<DiscountSuper> list = new ArrayList<>();
		list=getListWithoutNullOrDuplicates(list);
		assertEquals(0, list.size());
	}
	
	public ArrayList<DiscountSuper> getListWithoutNullOrDuplicates(ArrayList<DiscountSuper> discounts){
		HashSet<DiscountSuper> hs = new HashSet<DiscountSuper>();
		hs.addAll(discounts);
		discounts.clear();
		discounts.addAll(hs);
		while(discounts.contains(null)){
			discounts.remove(null);
		}
		return discounts;
	}
}

