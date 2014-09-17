package com.ajdeguzman.debtnote;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SinglePayment extends ActionBarActivity implements ActionBar.OnNavigationListener  {
	int DEBT_TYPE, DEBT_ID;
	String DATE_ENDED;
	Button btnTime, btnDate;
	TextView lblCurrentAmount;
	EditText txtPaymentAmount;
	TimePicker timePicker;
	DatePicker datePicker;
	int hour, minute;
	int OPERATOR;
	String pref_curr, dateTime;
	float current_amount, payment, diff;
	int yr, month, day, weekday;
	String currentDate, currentTime;
	static final int TIME_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID = 1;
	DebtDatabaseHandler db = new DebtDatabaseHandler(this);
	SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	DecimalFormat df = new DecimalFormat("#,###,###.00");
	String currency;
	private final static String[] DAYS_OF_WEEK = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" }; 
	private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_payment);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(40,57,91)));
		generateNavigationMenu();
		getPreferencesValue();
		currency = new ClassCurrency().getSymbols(Integer.parseInt(pref_curr));
		timePicker = (TimePicker) findViewById(R.id.timePicker);
		datePicker = (DatePicker) findViewById(R.id.datePicker);
		btnTime = (Button) findViewById(R.id.btnTime);
		btnDate = (Button) findViewById(R.id.btnDate);
		lblCurrentAmount = (TextView) findViewById(R.id.lblCurrentAmount);
		txtPaymentAmount = (EditText) findViewById(R.id.txtPaymentAmount);
		setCurrentTime();
		Calendar today = Calendar.getInstance();
		yr = today.get(Calendar.YEAR);
		month = today.get(Calendar.MONTH);
		day = today.get(Calendar.DAY_OF_MONTH);
		weekday = today.get(Calendar.DAY_OF_WEEK);
		txtPaymentAmount.addTextChangedListener(new TextWatcher(){
			@Override
			public void afterTextChanged(Editable s) {
				if(!txtPaymentAmount.getText().toString().trim().equalsIgnoreCase("") && !txtPaymentAmount.getText().toString().trim().equalsIgnoreCase(null)){
					switch(OPERATOR){
						case 0:
							float temp = current_amount;
							payment = Float.parseFloat(txtPaymentAmount.getText().toString());
							temp = temp + payment;
							if(currency.length() == 1){
								lblCurrentAmount.setText(currency + " " + df.format(temp));
						    }else{
						    	lblCurrentAmount.setText(df.format(temp) + " " + currency);
						    }
							break;
						case 1:
							float temp2 = current_amount;
							payment = Float.parseFloat(txtPaymentAmount.getText().toString());
							temp2 = temp2 - payment;
							if(currency.length() == 1){
								lblCurrentAmount.setText(currency + " " + df.format(temp2));
						    }else{
						    	lblCurrentAmount.setText(df.format(temp2) + " " + currency);
						    }
							break;
					}
				}else{
					if(currency.length() == 1){
						lblCurrentAmount.setText(currency + " " + df.format(current_amount));
				    }else{
				    	lblCurrentAmount.setText(df.format(current_amount) + " " + currency);
				    }
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});
	}
	private void addDebt(){
		if(payment > 0){
			float value = payment + current_amount;
			db.updateDebtPayment(String.valueOf(df.format(value)), DEBT_ID);
			db.addPayment(new ClassPayment(DEBT_ID, 0, String.valueOf(df.format(payment)), dateTime));
			goBack();
			showToast("Successfully Added");
		}
	}
	public void exceedAlert(){
 		AlertDialog.Builder bldr = new AlertDialog.Builder(this);
		bldr.setMessage("The payment is greater than the current debt.");
		bldr.setCancelable(true);
		bldr.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				}
			});
		final AlertDialog alt = bldr.create();
		alt.show();
 	}
	public void moveAlert(){
		AlertDialog.Builder bldr = new AlertDialog.Builder(this);
		bldr.setTitle("Move to Archive?");
		bldr.setMessage("This debt item will be mark as repaid and will be moved to archive.");
		bldr.setCancelable(true);
		bldr.setPositiveButton("Move", new DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int which) {
					db.updateDebtPayment(String.valueOf(df.format(diff)), DEBT_ID);
					db.addPayment(new ClassPayment(DEBT_ID, 1, String.valueOf(df.format(payment)), dateTime));
					moveToArchive();
				}
			});
		bldr.setNegativeButton("Don't Move", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					db.updateDebtPayment(String.valueOf(df.format(diff)), DEBT_ID);
					db.addPayment(new ClassPayment(DEBT_ID, 1, String.valueOf(df.format(payment)), dateTime));
					showToast("Successfully Deducted");
					goBack();
				}
			});
		final AlertDialog alt = bldr.create();
		alt.show();
	}
	private void moveToArchive(){
 		db.deletePaymentById(DEBT_ID);
		db.deleteDebtById(String.valueOf(DEBT_ID));
		db.updateHistoryEnd(new ClassHistory(
				DEBT_ID,
				DATE_ENDED));
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("position", 3);
		i.putExtras(bundle);
		startActivity(i);
		slideActivity(1);	
		finish();
 	}

 	public void slideActivity(int opt){
 		if (opt != 0){
 			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
 		}
 	}
	private void deductDebt(){
		if(payment > 0){
			if(payment > current_amount){
				exceedAlert();
			}else{
				diff = current_amount - payment;
				if(diff == 0){
					moveAlert();
				}
				if(diff != 0){
					db.updateDebtPayment(String.valueOf(df.format(diff)), DEBT_ID);
					db.addPayment(new ClassPayment(DEBT_ID, 1, String.valueOf(df.format(payment)), dateTime));
					goBack();
				}
			}
		}
	}
	private void payNow(){
		switch(OPERATOR){
			case 0:
				addDebt();
				break;
			case 1:
				deductDebt();
				break;
		}
	}
	private void getPreferencesValue(){
 		SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
 		pref_curr = appPrefs.getString("lstDefaultCurrency", "53");
	}
	public void clickTime(View v){
		timePicker.setIs24HourView(true);
		showDialog(TIME_DIALOG_ID);
	}
	public void clickDate(View v){
		showDialog(DATE_DIALOG_ID);
	}
 	public void showToast(String msg){
 		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
 	}
 	@Override
 	protected Dialog onCreateDialog(int id)
 	{
	 	switch (id) {
	 		case TIME_DIALOG_ID:
	 			return new TimePickerDialog(this, mTimeSetListener, hour, minute, false);
	 		case DATE_DIALOG_ID:
	 			return new DatePickerDialog(this, mDateSetListener, yr, month, day);
	 	}
	 	return null;
 	}
 	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener()
 	{
 		public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour)
 			{
 				hour = hourOfDay;
 				minute = minuteOfHour;
 				@SuppressWarnings("deprecation")
				Date date = new Date(0, 0, 0, hour, minute);
 				String strDate = timeFormat.format(date);
 				btnTime.setText(strDate);
 			}
 		};
 	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener()
 	{
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			yr = year;
			month = monthOfYear;
			day = dayOfMonth;
			SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
		    Date date = new Date(yr, month, day-1);
		    String dayOfWeek = simpledateformat.format(date).substring(0,3);
			btnDate.setText(new StringBuilder().append(dayOfWeek + ", ").append(MONTHS[month])
					   .append(" ").append(day).append(", ").append(yr)
					   .append(" "));
		}
 	};
	@Override
	public void onStart(){
		super.onStart();
		DATE_ENDED =  new SimpleDateFormat("dd MMM yy").format(new Date());
		if (null == currentDate && null == currentTime) {
				currentTime = new SimpleDateFormat("hh:mm aa").format(new Date());
				currentDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
 				btnTime.setText(currentTime);
 				btnDate.setText(new StringBuilder().append(DAYS_OF_WEEK[weekday - 1] + ", ").append(MONTHS[month])
 					   .append(" ").append(day).append(", ").append(yr)
 					   .append(" "));

 				dateTime = btnDate.getText() +  " " + btnTime.getText();
				
		}
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			DEBT_ID = extras.getInt("id");
			DEBT_TYPE = extras.getInt("type");
 			getSupportActionBar().setSelectedNavigationItem(extras.getInt("payment_type"));
 			List<ClassDebt> debts = db.getDebt(DEBT_ID);
 			for (ClassDebt dc : debts) {
 				current_amount = Float.parseFloat(dc.getDebtAmount().replace(",", ""));
 				if(currency.length() == 1){
 					lblCurrentAmount.setText(currency + " " + dc.getDebtAmount());
			    }else{
			    	lblCurrentAmount.setText(dc.getDebtAmount() + " " + currency);
			    }
 			}
		}
	}
	public void generateNavigationMenu(){
		ActionBar actionBar;
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		final String[] navListDebt = {"ADD","DEDUCT"}; 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(actionBar.getThemedContext(), R.layout.support_simple_spinner_dropdown_item, android.R.id.text1, navListDebt);
		adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(adapter, this);
	}
	private void goBack(){
		Intent i;
		Bundle bundle;
		i = new Intent(getApplicationContext(), SingleDebt.class);
    	bundle = new Bundle();
		bundle.putString("id", String.valueOf(DEBT_ID));
		bundle.putInt("type", DEBT_TYPE);
		i.putExtras(bundle);
		startActivity(i);	
		slideIt(1);	
		finish();
	}
 	public boolean onOptionsItemSelected(MenuItem item)
	{
	    switch (item.getItemId())
	    {
	    case android.R.id.home:
	    	goBack();
	        return true;
	    case R.id.action_save:
	    	payNow();
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	private void setCurrentTime(){
		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);
	}	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.single_payment, menu);
		return true;
	}

 	public void slideIt(int opt){
 		if (opt != 0){
 			overridePendingTransition(R.anim.slide_up,R.anim.no_change);
 		}
 	}
	@Override
	public boolean onNavigationItemSelected(int position, long arg1) {
		switch(position){
		case 0:
			OPERATOR = 0;
			break;
		case 1:
			OPERATOR = 1;
			break;
		}
		return false;
	}
}