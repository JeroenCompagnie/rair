package com.realdolmen.domain.flight.locationReaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.realdolmen.domain.flight.Airport;

public class CSVReader {

	@PersistenceContext
	EntityManager em;
	
    public static void main(String[] args) {
    	getAirportsFromCSV();
    }
    
    public static HashMap<String, Airport> getAirportsFromCSV(){
    	/**
    	 * Read country - global region
    	 */
        String csvFile = "src/main/resources/countries_global_regions.csv";
        String line = "";
        String cvsSplitBy = ";";
        
        HashMap<String, String> country_region = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                String[] country = line.split(cvsSplitBy);
                country_region.put(country[0].trim(), country[1].trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // To see if read properly
//        System.err.println(country_region.size());
//        printCountryRegionHashmap(country_region);
//        System.err.println(country_region.size());
        
        
        /**
         * Read city - country - code
         */
        csvFile = "src/main/resources/city_country_airportName_code.csv";
        
        HashMap<String, Airport> code_airport = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split(cvsSplitBy);
                if(lineIsValid(lineSplit)){
                	code_airport.put(lineSplit[2].trim(), 
                		new Airport(lineSplit[0].trim(), lineSplit[3].trim(),
                				lineSplit[4].trim(), lineSplit[2].trim(),
                				lineSplit[1].trim()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // To see if read properly
//        System.err.println(code_city_country.size());
//        printCode_City_Country_Hashmap(code_city_country);
//        System.err.println(code_city_country.size());
        
        /**
         * Add global region to each airport
         */
        int nrOfGlobalRegionsNotFound = 0;
        for(Map.Entry<String, Airport> entry : code_airport.entrySet()){
        	String matching_globalRegion = country_region.get(entry.getValue().getCountry());
        	if(matching_globalRegion == null){
        		nrOfGlobalRegionsNotFound++;
        		/**
        		 * TODO: change csv for remaining global regions
        		 */
//        		System.err.println(entry.getValue().getCountry());
        	}
        	else{
        		entry.getValue().setGlobalRegion(matching_globalRegion);
        	}
        }
       
        System.out.println(nrOfGlobalRegionsNotFound);
        
        int count = 0;
        for(Map.Entry<String, Airport> entry : code_airport.entrySet()){
        	if(null != entry.getValue().getGlobalRegion()){
        		count++;
        	}
        }
        System.err.println("HIER: " + count);
       
        return code_airport;
    }

	private static boolean lineIsValid(String[] lineSplit) {
		if(lineSplit.length < 6){
			return false;
		}
		for(String s:lineSplit){
			if(s.trim().equals("") || s.trim().toLowerCase().equals("city name")
					|| s.trim().toLowerCase().equals("airport code") 
					|| s.trim().toLowerCase().equals("country name")
					|| s.trim().toLowerCase().equals("country abbrev.")
					|| s.trim().toLowerCase().equals("world area code")){
				return false;
			}
		}
		return true;
	}

	private static void printCode_City_Country_Hashmap(HashMap<String, Airport> code_city_country) {
		for(Map.Entry<String, Airport> entry : code_city_country.entrySet()){ 
			System.out.printf("Code : %s and City - Country - Name - Code: %s - %s - %s - %s %n", entry.getKey(), 
					entry.getValue().getCity(), entry.getValue().getCountry(), entry.getValue().getAirportName(), entry.getValue().getCountryCode());
		}
	}

	private static void printCountryRegionHashmap(HashMap<String, String> country_region) {
		for(Map.Entry<String, String> entry : country_region.entrySet()){ 
			System.out.printf("Coutnry : %s and Region: %s %n", entry.getKey(), entry.getValue());
		}

		
	}
    
}