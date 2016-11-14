package com.realdolmen.dateConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConverter {
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	
	public static String format(Date date){
		return formatter.format(date);
	}
	
	/**
	 * Returns Date object with given day, month and year. Returns null if impossible date.
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	public static Date getDate(int day, int month, int year){
		try{
			Calendar cal = Calendar.getInstance();
			cal.set(year, month-1, day);
			return cal.getTime();
		}
		catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	/**
	 * Returns date x days before today. If a negative number is given it gives the date X days after today.
	 * @param x
	 * @return
	 */
	public static Date getDateXDaysBeforeToday(int x){
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.DAY_OF_YEAR,-x);
		return cal.getTime();
	}
	
	/**
	 * Returns date x days after today. If a negative number is given it gives the date X days before today.
	 * @param x
	 * @return
	 */
	public static Date getDateXDaysAfterToday(int x){
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.DAY_OF_YEAR,+x);
		return cal.getTime();
	}
}
