package com.realdolmen.domain.flight;

public enum PaymentMethod {
	CreditCard(5),Endorsement(0);
	
	private int discount;
	
	PaymentMethod(int discount)
	{
		this.discount=discount;
	}
	
	@Override
	public String toString()
	{
		if(this.discount!=0)
		{
		return this.name() + " (" + this.discount + " % discount)";
		}
		else return this.name();
	}
}
