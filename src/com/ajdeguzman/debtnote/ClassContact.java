package com.ajdeguzman.debtnote;

import android.graphics.Bitmap;


public class ClassContact {
	int _id;
	private String _name;
	private String _phone;
	private String _email;
	private String _address;
	private Bitmap _picture;
	public ClassContact(){
		
	}
	public ClassContact(int id, String _name, String _phone_number, String _email, String _address, Bitmap _picture){
		this._id = id;
		this._name = _name;
		this._phone = _phone_number;
		this._email = _email;
		this._address= _address;
		this._picture = _picture;
	}
	
	public ClassContact(String name, String _phone_number, String _email, String _address, Bitmap _picture){
		this._name = name;
		this._phone = _phone_number;
		this._email = _email;
		this._address = _address;
		this._picture = _picture;
	}
	public int getID(){
		return this._id;
	}
	public void setID(int id){
		this._id = id;
	}
	public String getName(){
		return this._name;
	}
	public void setName(String _name){
		this._name = _name;
	}
	public String getPhone(){
		return this._phone;
	}
	public void setPhone(String _phone){
		this._phone = _phone;
	}
	public String getEmail(){
		return this._email;
	}
	public void setEmail(String _email){
		this._email = _email;
	}
	public String getAddress(){
		return this._address;
	}
	public void setAddress(String address){
		this._address = address;
	}
	public Bitmap getPicture(){
		return this._picture;
	}
	public void setPicture(Bitmap _picture){
		this._picture = _picture;
	}
}
