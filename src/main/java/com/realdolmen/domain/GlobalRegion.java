package com.realdolmen.domain;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public enum GlobalRegion implements Serializable{
	WesternEurope, EasternEurope, SouthernEurope, NorthernEurope, 
	MiddleEast,
	NorthAmerica, CentralAmerica, SouthAmerica, 
	Africa, 
	Asia,
	Oceania
}
