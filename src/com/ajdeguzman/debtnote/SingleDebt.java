package com.ajdeguzman.debtnote;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SingleDebt extends ActionBarActivity {

	int DEBT_TYPE, DEBT_ID;
	float DEBT_AMOUNT;
	String DATE_ENDED;
	DateFormat formatter = null;
    Date convertedDate = null;
	String pref_curr;
	float payment, diff;
	String currentDate, currentTime, dateTime;
	int yr, month, day, weekday;
	ListView lstPaymentHistory;
	DebtDatabaseHandler db = new DebtDatabaseHandler(this);
	TextView singlePerson,singleDue,singleDate,singleAmount,singleDescription, txtEmpty;
	DecimalFormat df = new DecimalFormat("#,###,###.00"); 
	private final static String[] DAYS_OF_WEEK = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" }; 
	private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_debt);
		getPreferencesValue();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(40,57,91)));
		singlePerson = (TextView)findViewById(R.id.singlePerson);
		singleDue = (TextView)findViewById(R.id.singleDue);
		singleDate = (TextView)findViewById(R.id.singleDate);
		singleAmount = (TextView)findViewById(R.id.singleAmount);
		txtEmpty = (TextView)findViewById(R.id.txtEmpty);
		singleDescription = (TextView)findViewById(R.id.singleDescription);
		lstPaymentHistory = (ListView)findViewById(R.id.lstPaymentHistory);
		Calendar today = Calendar.getInstance();
		yr = today.get(Calendar.YEAR);
		month = today.get(Calendar.MONTH);
		day = today.get(Calendar.DAY_OF_MONTH);
		weekday = today.get(Calendar.DAY_OF_WEEK);
	}
	private void getPreferencesValue(){
 		SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
 		pref_curr = appPrefs.getString("lstDefaultCurrency", "53");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.single_debt, menu);
		return true;
	}
 	public void showToast(String msg){
 		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
 	}
	@Override
	public void onStart(){
		super.onStart();
		Bundle extras = getIntent().getExtras();
		DATE_ENDED =  new SimpleDateFormat("dd MMM yy").format(new Date());
		if (null == currentDate && null == currentTime) {
			StringBuilder currDate;
			currentTime = new SimpleDateFormat("hh:mm aa").format(new Date());
			currentDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
			currDate = new StringBuilder().append(DAYS_OF_WEEK[weekday - 1] + ", ").append(MONTHS[month])
					   .append(" ").append(day).append(", ").append(yr)
					   .append(" ");

				dateTime = currDate +  " " + currentTime;
			
		}
		if (extras != null) {
			DEBT_ID = Integer.parseInt(extras.getString("id"));
			DEBT_TYPE = extras.getInt("type");
			List<ClassDebt> debts = db.getDebt(DEBT_ID);
			for (ClassDebt dc : debts) {
				String date = dc.getDebtDate();
				String currency = new ClassCurrency().getSymbols(Integer.parseInt(pref_curr));
		        formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		        try {
					convertedDate = (Date) formatter.parse(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				singlePerson.setText(dc.getDebtPerson().toString());
				if(dc.getDebtDue().toString().trim().length() != 0){
					singleDue.setText("Due: " + dc.getDebtDue().substring(4, dc.getDebtDue().length()));
				}
				singleDate.setText(new PrettyDate(convertedDate).toString());
				DEBT_AMOUNT = Float.parseFloat(dc.getDebtAmount().replace(",",""));
			    if(currency.length() == 1){
					if(Float.parseFloat(dc.getDebtAmount().replace(",","")) != 0){
						if(DEBT_TYPE == 0){
							singleAmount.setText("You owe " + currency + " " + dc.getDebtAmount());
						}else{
							singleAmount.setText("You are owed " + currency + " " + dc.getDebtAmount());
						}
					}else{
						singleAmount.setText("\u2713 Repaid");
					}
			    }else{
					if(Float.parseFloat(dc.getDebtAmount().replace(",","")) != 0){
						if(DEBT_TYPE == 0){
							singleAmount.setText("You owe " + currency + " " + dc.getDebtAmount());
						}else{
							singleAmount.setText("You are owed " + dc.getDebtAmount() + " " + currency);
						}
					}else{
						singleAmount.setText("\u2713 Repaid");
					}
					
			    }
			    if(dc.getDebDesc().trim().length() != 0){
			    	singleDescription.setText(dc.getDebDesc().toString());
			    }else{
			    	singleDescription.setText("No Description");
			    }
				
			}
			populatePaymentHistory();
		}
	}
	private void populatePaymentHistory(){ 
		 String currency = new ClassCurrency().getSymbols(Integer.parseInt(pref_curr));
		 List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		 List<ClassPayment> payments = db.getAllPaymentWhere(DEBT_ID);
		 for (ClassPayment pa : payments){
				 if(pa.getPaymentAmount() != null){
				    Map<String, Object> datum = new HashMap<String, Object>();
					datum.put("date", pa.getPaymentDate());
					if(pa.getPaymentType() == 0){
						datum.put("amount", "+ " + currency + " " + pa.getPaymentAmount());
						datum.put("color", BitmapFactory.decodeResource(getResources(), R.drawable.ic_debt_out));
					}else{
						datum.put("amount", "- " + currency + " " + pa.getPaymentAmount());
						datum.put("color", BitmapFactory.decodeResource(getResources(), R.drawable.ic_debt_in));
					}
				    data.add(datum);
				 }
			}
	        ExtendedSimpleAdapter paymentAdapter = new ExtendedSimpleAdapter (getApplicationContext(), data, R.layout.payment_history_layout,
	                new String[] {"amount", "date","color"},
	                new int[] {R.id.amount, R.id.date, R.id.img});
	        lstPaymentHistory.setAdapter(paymentAdapter);
	    	if(paymentAdapter.getCount() == 0){
				txtEmpty.setText("History is Empty.");
			}else{
				txtEmpty.setText("");
			}
	}
 	public void slideIt(int opt){
 		if (opt != 0){
 			overridePendingTransition(R.anim.slide_down,R.anim.no_change);
 		}
 	}
 	public void slideActivity(int opt){
 		if (opt != 0){
 			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
 		}
 	}
 	private void moveToArchive(){
 		db.deletePaymentById(DEBT_ID);
		db.deleteDebtById(String.valueOf(DEBT_ID));
		db.updateHistoryEnd(new ClassHistory(DEBT_ID,DATE_ENDED));
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("position", 3);
		i.putExtras(bundle);
		startActivity(i);
		slideActivity(1);	
		finish();
 	}
 	public void markAsReturned(){
 		AlertDialog.Builder bldr = new AlertDialog.Builder(this);
		bldr.setTitle("Move to Archive?");
		bldr.setMessage("This debt item will be mark as repaid and will be moved to archive.");
		bldr.setCancelable(true);
		bldr.setNeutralButton("Don't Move", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				payment = DEBT_AMOUNT;
				diff = DEBT_AMOUNT - payment;
				db.updateDebtPayment(String.valueOf(df.format(diff)), DEBT_ID);
				db.addPayment(new ClassPayment(DEBT_ID, 1, String.valueOf(df.format(payment)), dateTime));
				showToast("Debt Repaid");
				goBack();	
			}
		});
		bldr.setPositiveButton("Move", new DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int which) {
					moveToArchive();
				}
			});
		bldr.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();			
				}
			});
		final AlertDialog alt = bldr.create();
		alt.show();
 	}
 	public void markAsReturnedRepaid(){
 		AlertDialog.Builder bldr = new AlertDialog.Builder(this);
		bldr.setTitle("Move to Archive?");
		bldr.setMessage("This repaid item will be moved to archive.");
		bldr.setCancelable(true);
		bldr.setPositiveButton("Move", new DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int which) {
					moveToArchive();
				}
			});
		bldr.setNegativeButton("Don't Move", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
		final AlertDialog alt = bldr.create();
		alt.show();
 	}
 	
	public void noDeductAlert(){
 		AlertDialog.Builder bldr = new AlertDialog.Builder(this);
		bldr.setMessage("This item has been repaid");
		bldr.setCancelable(true);
		bldr.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
		final AlertDialog alt = bldr.create();
		alt.show();
 	}
	private void goBack(){
		Intent i;
		Bundle bundle;
		i = new Intent(getApplicationContext(), MainActivity.class);
    	bundle = new Bundle();
    	bundle.putInt("position", DEBT_TYPE+1);
		i.putExtras(bundle);
		startActivity(i);	
		slideActivity(1);	
		finish();
	}
	@Override
 	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent i;
		Bundle bundle;
	    switch (item.getItemId())
	    {
	    case android.R.id.home:
	    	i = new Intent(getApplicationContext(), MainActivity.class);
			bundle = new Bundle();
			bundle.putInt("position", DEBT_TYPE+1);
			i.putExtras(bundle);
			startActivity(i);	
			finish();
	        return true;
	    case R.id.action_paid:
	    	if(DEBT_AMOUNT != 0){
	    		markAsReturned();
	    	}else{
	    		markAsReturnedRepaid();
	    	}
	    	return true;
	    case R.id.action_add_payment:
	    	i = new Intent(getApplicationContext(), SinglePayment.class);
			bundle = new Bundle();
			bundle.putInt("id", DEBT_ID);
			bundle.putInt("type", DEBT_TYPE);
			bundle.putInt("payment_type", 0);
			i.putExtras(bundle);
			startActivity(i);	
			slideIt(1);
			finish();
	        return true;
	    case R.id.action_minus_payment:
	    	if(DEBT_AMOUNT != 0){
	    		i = new Intent(getApplicationContext(), SinglePayment.class);
				bundle = new Bundle();
				bundle.putInt("id", DEBT_ID);
				bundle.putInt("type", DEBT_TYPE);
				bundle.putInt("payment_type", 1);
				i.putExtras(bundle);
				startActivity(i);	
				slideIt(1);
				finish();
	    	}else{
	    		noDeductAlert();
	    	}
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

}
