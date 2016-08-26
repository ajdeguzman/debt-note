/**
 * Author: Aljohn De Guzman on 8/26/2016.
 * WebOutsourcing Gateway Inc.
 * adeguzman@weboutsourcing-gateway.com
 */

package com.ajdeguzman.debtnote.model;

import io.realm.RealmObject;

public class Debt extends RealmObject {

    int _id;
    String _debt_amount;
    String _debt_date;
    String _debt_due;
    int _debt_type;
    String _debt_person;
    String _debt_desc;
    String _debt_due_parse;

    public Debt() {

    }
    public Debt(int id, String _debt_amount, String _debt_date, String _debt_due, int _debt_type, String _debt_person, String _debt_desc, String _debt_due_parse){
        this._id = id;
        this._debt_amount = _debt_amount;
        this._debt_date = _debt_date;
        this._debt_due = _debt_due;
        this._debt_type= _debt_type;
        this._debt_person= _debt_person;
        this._debt_desc=_debt_desc;
        this._debt_due_parse = _debt_due_parse;

    }

    public Debt(String _debt_amount, String _debt_date, String _debt_due, int _debt_type, String _debt_person, String _debt_desc, String _debt_due_parse){
        this._debt_amount = _debt_amount;
        this._debt_date = _debt_date;
        this._debt_due = _debt_due;
        this._debt_type= _debt_type;
        this._debt_person= _debt_person;
        this._debt_desc= _debt_desc;
        this._debt_due_parse = _debt_due_parse;
    }
    public int getID(){
        return this._id;
    }
    public void setID(int id){
        this._id = id;
    }
    public String getDebtAmount(){
        return this._debt_amount;
    }
    public void setDebtAmount(String _debt_amount){
        this._debt_amount = _debt_amount;
    }
    public String getDebtDate(){
        return this._debt_date;
    }
    public void setDebtDate(String _debt_date){
        this._debt_date = _debt_date;
    }
    public String getDebtDue(){
        return this._debt_due;
    }
    public void setDateDue(String _debt_due){
        this._debt_due = _debt_due;
    }
    public int getDebtType(){
        return this._debt_type;
    }
    public void setDebtType(int _debt_type){
        this._debt_type = _debt_type;
    }
    public String getDebtPerson(){
        return this._debt_person;
    }
    public void setDebtPerson(String _debt_person){
        this._debt_person = _debt_person;
    }
    public String getDebDesc(){
        return this._debt_desc;
    }
    public void setDebtDesc(String _debt_desc){
        this._debt_desc = _debt_desc;
    }

    public String getDebtDueParse(){
        return this._debt_due_parse;
    }
    public void setDebtDueParse(String _debt_due_parse){
        this._debt_due_parse = _debt_due_parse;
    }

}
