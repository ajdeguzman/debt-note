package com.ajdeguzman.debtnote;

import android.graphics.Bitmap;


public class ClassHistory {
	int id;
	String hist_desc;
	String hist_person;
	String hist_initial;
	String hist_created;
	String hist_ended;
	int hist_id;
	private Bitmap hist_photo;
	public ClassHistory(){
		
	}
	public ClassHistory(int id, String hist_ended){
		this.id = id;
		this.hist_ended = hist_ended;
	}
	public ClassHistory(int id, String hist_desc, String hist_person, String hist_initial, String hist_created, Bitmap hist_photo){
		this.id = id;
		this.hist_desc = hist_desc;
		this.hist_person = hist_person;
		this.hist_initial = hist_initial;
		this.hist_created = hist_created;
		this.hist_photo = hist_photo;
	}
	public int getID(){
		return this.id;
	}
	public void setID(int id){
		this.id = id;
	}
	public int getHistID(){
		return this.hist_id;
	}
	public void setHistID(int hist_id){
		this.hist_id = hist_id;
	}
	public String getDesc(){
		return this.hist_desc;
	}
	public void setDesc(String hist_desc){
		this.hist_desc = hist_desc;
	}
	public String getPerson(){
		return this.hist_person;
	}
	public void setPerson(String hist_person){
		this.hist_person = hist_person;
	}
	public String getInitial(){
		return this.hist_initial;
	}
	public void setInitial(String hist_initial){
		this.hist_initial = hist_initial;
	}
	public String getCreated(){
		return this.hist_created;
	}
	public void setCreated(String hist_created){
		this.hist_created = hist_created;
	}
	public String getEnded(){
		return this.hist_ended;
	}
	public void setEnded(String hist_ended){
		this.hist_ended = hist_ended;
	}
	public Bitmap getPicture(){
		return this.hist_photo;
	}
	public void setPicture(Bitmap hist_photo){
		this.hist_photo = hist_photo;
	}
}
