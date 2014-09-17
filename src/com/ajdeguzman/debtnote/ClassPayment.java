package com.ajdeguzman.debtnote;


public class ClassPayment {
	int id;
	int payment_type;
	String payment_amount;
	String payment_date;
	public ClassPayment(){
		
	}
	public ClassPayment(int id, int payment_type, String payment_amount, String payment_date){
		this.id = id;
		this.payment_type = payment_type;
		this.payment_amount = payment_amount;
		this.payment_date = payment_date;
	}
	
	public ClassPayment(int id, int payment_type){
		this.id = id;
		this.payment_type = payment_type;
	}
	public int getID(){
		return this.id;
	}
	public void setID(int id){
		this.id = id;
	}
	public int getPaymentType(){
		return this.payment_type;
	}
	public void setPaymentType(int payment_type){
		this.payment_type = payment_type;
	}
	public String getPaymentAmount(){
		return this.payment_amount;
	}
	public void setPaymentAmount(String payment_amount){
		this.payment_amount = payment_amount;
	}
	public String getPaymentDate(){
		return this.payment_date;
	}
	public void setPaymentDate(String payment_date){
		this.payment_date = payment_date;
	}
}
