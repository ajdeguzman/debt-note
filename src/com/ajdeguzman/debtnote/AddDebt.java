package com.ajdeguzman.debtnote;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ajdeguzman.debtnote.service.NotifyService;
import com.ajdeguzman.debtnote.service.ScheduleClient;

public class AddDebt extends ActionBarActivity implements ActionBar.OnNavigationListener {
	EditText txtDebtAmount, txtMobileNo, txtDescription, txtEmail;
	String PICKER_DATE_VALUE = "", PICKER_TIME_VALUE = "", DATE_DUE_PARSE = "";
	AutoCompleteTextView txtPerson;
    ArrayList<Map<String, String>> mPeopleList;
	LinearLayout layoutViewMore, layoutViewMoreEmail;
	RelativeLayout layoutAddMore;
	ExtendedSimpleAdapter adapter;
	TextView lblName, lblCurrency, imgViewMore;
	Button btnSave, btnTime, btnDate;
	Bitmap my_btmp;
	Bundle extras;
	int DEBT_TYPE, DEBT_ID;
	boolean isSave = true;
	boolean open = false;
	private final int PICK = 1;
	String IMG_DIR = null;
    String contactNumber = null;
    String contactName = null;
    String contactEmail = null;
	String pref_curr;
	String DATE_TODAY;
	String MOBILE_NO, CONTACT_NAME, DUE_DATE, DATE_CREATED, AMOUNT, DEBT_DESC, DEBT_CURRENCY, CONTACT_EMAIL, HIST_DATE;
	TimePicker timePicker;
	DatePicker datePicker;
	int hour, minute;
	String name = null;
	int yr, month, day, weekday;
	String currentDate, currentTime;
	static final int TIME_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID = 1;
	SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private final static String[] DAYS_OF_WEEK = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" }; 
	private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	DebtDatabaseHandler db = new DebtDatabaseHandler(this);
    ScheduleClient scheduleClient = new ScheduleClient(this);
    NotifyService notifyService = new NotifyService();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_debt);
		scheduleClient.doBindService();
		generateNavigationMenu();
		txtPerson = (AutoCompleteTextView)findViewById(R.id.txtPerson);
		txtMobileNo = (EditText)findViewById(R.id.txtMobileNo);
		txtEmail = (EditText)findViewById(R.id.txtEmail);
		txtDebtAmount = (EditText)findViewById(R.id.txtDebtAmount);
		txtDescription = (EditText)findViewById(R.id.txtDescription);
		lblCurrency = (TextView)findViewById(R.id.lblCurrency);
		lblName = (TextView)findViewById(R.id.lblName);
		btnSave = (Button)findViewById(R.id.btnSave);
		layoutViewMore = (LinearLayout)findViewById(R.id.layoutViewMore);
		layoutViewMoreEmail = (LinearLayout)findViewById(R.id.layoutViewMoreEmail);
		layoutAddMore = (RelativeLayout)findViewById(R.id.layoutAddMore);
		imgViewMore = (TextView)findViewById(R.id.imgViewMore);
		timePicker = (TimePicker) findViewById(R.id.timePicker);
		datePicker = (DatePicker) findViewById(R.id.datePicker);
		btnTime = (Button) findViewById(R.id.btnTime);
		btnDate = (Button) findViewById(R.id.btnDate);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(40,57,91)));
		//getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
		setCurrentDate();
		setCurrentTime();
		getPreferencesValue();
		setAutoComplete();
		Calendar today = Calendar.getInstance();
		yr = today.get(Calendar.YEAR);
		month = today.get(Calendar.MONTH);
		day = today.get(Calendar.DAY_OF_MONTH);
		weekday = today.get(Calendar.DAY_OF_WEEK);
		txtPerson.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		txtPerson.setOnTouchListener(new View.OnTouchListener(){
			   @Override
			   public boolean onTouch(View v, MotionEvent event){
				  if(TextUtils.isEmpty(txtPerson.getText())){
					  txtPerson.showDropDown();
				  }
			      return false;
			   }
			});
		txtPerson.setOnItemClickListener(new OnItemClickListener() { 
			@Override
			public void onItemClick(AdapterView<?> av, View arg1, int index, long arg3) {
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) av.getItemAtPosition(index);
				String name = map.get("name");
				String number = map.get("phone");
				String email = map.get("email");
				txtPerson.setText(name); 
				txtMobileNo.setText(number);
				txtEmail.setText(email);
				} 
			}); 
		lblCurrency.setText(new ClassCurrency().getSymbols(Integer.parseInt(pref_curr)));

		if (null == currentDate && null == currentTime) {
			currentTime = new SimpleDateFormat("hh:mm aa").format(new Date());
			currentDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
				btnTime.setHint(currentTime);
				btnDate.setHint(new StringBuilder().append(DAYS_OF_WEEK[weekday - 1] + ", ").append(MONTHS[month])
					   .append(" ").append(day).append(", ").append(yr)
					   .append(" "));
			
		}
			extras = getIntent().getExtras();
			if (extras != null) {
				DEBT_ID = Integer.parseInt(extras.getString("id"));
				List<ClassDebt> debts = db.getDebt(DEBT_ID);
				for (ClassDebt dc : debts) {
					name = dc.getDebtPerson().toString();
					txtPerson.setText(name);
					txtDebtAmount.setText(dc.getDebtAmount().replaceAll(",", "").toString());
					btnDate.setText(dc.getDebtDue().toString());
					txtDescription.setText(dc.getDebDesc().toString());
					if(!TextUtils.isEmpty(dc.getDebtDueParse().toString())){
			 			String[] DATETIME_PARSED  = dc.getDebtDueParse().split("\\.");
				    	int year_n = 0,month_n = 0,day_n = 0,hour_n = 0,minute_n = 0;
				    	year_n = Integer.parseInt(DATETIME_PARSED[0]);
				    	month_n = Integer.parseInt(DATETIME_PARSED[1]);
				    	day_n = Integer.parseInt(DATETIME_PARSED[2]);
				    	PICKER_DATE_VALUE = year_n + "." + month_n + "." + day_n;
				    	if(DATETIME_PARSED.length > 3){
				    		hour_n = Integer.parseInt(DATETIME_PARSED[3]);
				    		minute_n = Integer.parseInt(DATETIME_PARSED[4]);
				    	  PICKER_TIME_VALUE = hour_n + "." + minute_n;
				    	  Date date = new Date(0, 0, 0, hour_n, minute_n);
		 				  String strDate = timeFormat.format(date);
		 				  btnTime.setText(strDate);
				    	}
		 			}
				}
				List<ClassContact> contacts = db.getContactByName(name);
		  		for (ClassContact cn : contacts) {
					txtEmail.setText( cn.getEmail());
					txtMobileNo.setText(cn.getPhone());
		  		}
	 			getSupportActionBar().setSelectedNavigationItem(extras.getInt("type"));
				btnSave.setText("Update");
				isSave = false;
			}
	}
	private void getPreferencesValue(){
 		SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 		pref_curr = appPrefs.getString("lstDefaultCurrency", "53");
	}	
	private void setAutoComplete(){
		mPeopleList = new ArrayList<Map<String, String>>();
		 List<ClassContact> contacts = db.getAllContact();  
		 List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			for (ClassContact cn : contacts) {
			    Map<String, Object> datum = new HashMap<String, Object>();
			    datum.put("picture", cn.getPicture());
			    datum.put("name", cn.getName());
			    datum.put("phone", cn.getPhone());
			    datum.put("email", cn.getEmail());
			    data.add(datum);
			}
		adapter = new ExtendedSimpleAdapter(this, data, R.layout.custom_autocomplete, new String[] {"name", "phone", "picture"},
				  new int[] { R.id.name, R.id.mobile, R.id.picture});
		txtPerson.setAdapter(adapter); 
	}
	public void clickTime(View v){
		timePicker.setIs24HourView(true);
		showDialog(TIME_DIALOG_ID);
	}
	public void clickDate(View v){
		showDialog(DATE_DIALOG_ID);
	}
    @Override
    protected void onStop() {
    	if(scheduleClient != null)
    		scheduleClient.doUnbindService();
    	super.onStop();	
    }
	@Override
	public void onStart(){
		super.onStart();
	}
	public void generateNavigationMenu(){
		ActionBar actionBar;
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		final String[] navListDebt = getResources().getStringArray(R.array.nav_list_debt); 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(actionBar.getThemedContext(), R.layout.support_simple_spinner_dropdown_item, android.R.id.text1, navListDebt);
		adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(adapter, this);
	}
	@SuppressWarnings("deprecation")
	public void clickViewCalendar(View v){
		showDialog(DATE_DIALOG_ID);
	}
	private void clearTextFields(){
		txtPerson.setText(null);
		txtMobileNo.setText(null);
		txtDebtAmount.setText(null);
		txtDescription.setText(null);
		txtEmail.setText(null);
	}
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		switch (reqCode) {
		case (PICK):
			if (resultCode == Activity.RESULT_OK) {
				 try{
					 Uri contactData = data.getData();
			            ContentResolver cr = getContentResolver();
			            Cursor c = cr.query(contactData, null, null, null, null);
			            if (c.moveToFirst()) {
			                String id = c
			                        .getString(c
			                                .getColumnIndexOrThrow(ContactsContract.Contacts._ID));

			                String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			                if (hasPhone.equalsIgnoreCase("1")) {
			                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
			                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
			                                            + " = " + id, null, null);
			                    Cursor emailCur = getContentResolver().query(Email.CONTENT_URI,  null, Email.CONTACT_ID + "=?", new String[] { id }, null);
			                    int emailId = emailCur.getColumnIndex(Email.DATA);
			                    if(emailCur.moveToFirst()){
			                    	contactEmail = emailCur.getString(emailId);
				                }
			                    if(phones.moveToFirst()){
			                    	contactNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				                }
		                    	contactName = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
			                    String num = contactNumber.toString();
			                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(id));
			                    InputStream photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), uri);
			                    BufferedInputStream buf = new BufferedInputStream(photo_stream);
			                    Bitmap my_btmp = BitmapFactory.decodeStream(buf);
			                    IMG_DIR = String.valueOf(my_btmp);
			                    txtPerson.setText(contactName.toString());
			                    txtMobileNo.setText(num.toString());
			                    txtEmail.setText(contactEmail.toString());
			                }
					}
				 }catch(Exception e){
					 
				 }
			}
			break;
		}
	}
	public void clickimgBrowseContacts(View v){
		Intent intent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, PICK);
	}
	public void clickLayoutAddMore(View v){
			Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
			layoutViewMore.setVisibility(View.VISIBLE);
			layoutViewMoreEmail.setVisibility(View.VISIBLE);
			layoutAddMore.setVisibility(View.GONE);
			layoutViewMoreEmail.startAnimation(slide);
			layoutViewMore.startAnimation(slide);
	}
	
	public void clickSave(View v){
 		if(isEmpty(txtPerson)){
 			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
 	        findViewById(R.id.txtPerson).startAnimation(shake);
 	        showToast("Please check required fields");
 		}
 		if(isEmpty(txtDebtAmount)){
 			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
 	        findViewById(R.id.txtDebtAmount).startAnimation(shake);
 		}
 		if(!isEmpty(txtDebtAmount) && !isEmpty(txtPerson)){
 			if(!txtDebtAmount.getText().toString().equals(".")){
 				if(Float.parseFloat(txtDebtAmount.getText().toString()) != 0){
 					String date = null;
 					String hist_date = null;
 		 			double u = Double.parseDouble(txtDebtAmount.getText().toString());
 		 		    DecimalFormat format = new DecimalFormat("#,###,###.00");
 		 		    String txtamount = format.format(u);
 		 			if (null == date) {
 		 				date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date());
 		 				hist_date = new SimpleDateFormat("dd MMM yy").format(new Date());
 		 			}
 		 			DEBT_CURRENCY = String.valueOf(lblCurrency.getText());
 		 			CONTACT_NAME = txtPerson.getText().toString();
 		 			DEBT_DESC = txtDescription.getText().toString();
 		 			AMOUNT = txtamount;
 		 			DUE_DATE = btnDate.getText().toString();
 		 			DATE_CREATED = date;
 		 			HIST_DATE = hist_date;
 		 	        MOBILE_NO = txtMobileNo.getText().toString();
 		 	        CONTACT_EMAIL = txtEmail.getText().toString();
 		 	        if(!TextUtils.isEmpty(btnDate.getText().toString())){
 		 	        	DATE_DUE_PARSE =  PICKER_DATE_VALUE + "." + PICKER_TIME_VALUE;
 		 	        }
 		 	    	Bitmap dp;
 		 	    	if(MOBILE_NO.toString().trim().length() != 0){
 							dp = getPhoto(MOBILE_NO);
 						}else{
 							dp = BitmapFactory.decodeResource(getResources(), R.drawable.user);
 						}
  		    	     List<ClassContact> contacts = db.getAllContact();  
  		    	     boolean exist = false;
 		 		     if(isSave){
 		 		    	    	try{
 				 	 		  		for (ClassContact cn : contacts) {
 				 	 		  		    if(cn.getName().equals(CONTACT_NAME)){
 				 	 		  		    	exist = true;
 				 	 		  		    }
 				 	 		  		}
 				  		    	 	if(exist){
 				  		    	 		db.addDebt(new ClassDebt(AMOUNT,
 				     	 											DATE_CREATED,
 				     	 											DUE_DATE,
 				     	 											DEBT_TYPE,
 				     	 											CONTACT_NAME,
 				     	 											DEBT_DESC,
 				     	 											DATE_DUE_PARSE));
 				  		    	 		db.addHistory(new ClassHistory(
 				  		    	 			db.getLastInserted(),
 				  		    	 			DEBT_DESC,
 				  		    	 			CONTACT_NAME,
 				  		    	 			AMOUNT,
 				  		    	 			HIST_DATE,
			 									dp));
 				  		    	 	callsNotificationSetter();
 				  		    	 	}else{
 				  		    	 		db.addDebt(new ClassDebt(AMOUNT,
 				 									DATE_CREATED,
 				 									DUE_DATE,
 				 									DEBT_TYPE,
 				 									CONTACT_NAME,
 				 									DEBT_DESC,
 				 									DATE_DUE_PARSE));
 				  		    	 		db.addContact(new ClassContact(
 			  		    	 					CONTACT_NAME,
 			 									MOBILE_NO,
 			 									CONTACT_EMAIL,
 			 									"",
 			 									dp));
 				  		    	 		db.addHistory(new ClassHistory(
 				  		    	 			db.getLastInserted(),
 				  		    	 			DEBT_DESC,
 				  		    	 			CONTACT_NAME,
 				  		    	 			AMOUNT,
 				  		    	 			HIST_DATE,
			 									dp));
 				  		    	 	callsNotificationSetter();
 				  		    	 	}
 				  		    	    clearTextFields();
 				  		    	    showToast("Debt Saved");
 				  		    		Intent i = new Intent(getApplicationContext(), MainActivity.class);
 				  					Bundle bundle = new Bundle();
 				  					bundle.putInt("position", DEBT_TYPE+1);
 				  					i.putExtras(bundle);
 				  					startActivity(i);
 		                	 		slideIt(1);	
 				  					finish();
 				  		       }catch(SQLException e){
 				  		    	   showToast("Contact Saving failed");
 				  		       }
 		 		     }else{
 			 		    	 try{
 			 		    		for (ClassContact cn : contacts) {
				 	 		  		    if(cn.getName().equals(CONTACT_NAME)){
				 	 		  		    	exist = true;
				 	 		  		    }
				 	 		  		}
 			 		    		if(exist){
 			 		    			db.updateDebt(new ClassDebt(DEBT_ID,
    	 									AMOUNT,
 											DATE_CREATED,
 											DUE_DATE,
 											DEBT_TYPE,
 											CONTACT_NAME,
 											DEBT_DESC,
		 									DATE_DUE_PARSE));
 			 		    			db.updateHistory(new ClassHistory(
					    	 				DEBT_ID,
					    	 				DEBT_DESC,
						    	 			CONTACT_NAME,
						    	 			AMOUNT,
						    	 			HIST_DATE,
											dp));
 			 		    		}else{
 			 		    			db.updateDebt(new ClassDebt(DEBT_ID,
    	 									AMOUNT,
 											DATE_CREATED,
 											DUE_DATE,
 											DEBT_TYPE,
 											CONTACT_NAME,
 											DEBT_DESC,
		 									DATE_DUE_PARSE));
 			 		    			db.updateDebtPerson(CONTACT_NAME, name);
 			 		    			db.updateContact(new ClassContact(
				    	 					CONTACT_NAME,
											MOBILE_NO,
											CONTACT_EMAIL,
											"",
											dp), name);
 			 		    			db.updateHistory(new ClassHistory(
					    	 				DEBT_ID,
					    	 				DEBT_DESC,
						    	 			CONTACT_NAME,
						    	 			AMOUNT,
						    	 			HIST_DATE,
											dp));
 			 		    		}
			 		    		 		
						    	 	callsNotificationSetter();
						    	    clearTextFields();
						    	    showToast("Debt Updated");
						    		Intent i = new Intent(getApplicationContext(), MainActivity.class);
									Bundle bundle = new Bundle();
									bundle.putInt("position", DEBT_TYPE+1);
									i.putExtras(bundle);
									startActivity(i);
									slideIt(1);	
									finish();
 			 		    	 }catch(SQLException e){
 			 		    		 showToast("Contact Saving failed");
 			 		    	 }
 		 		     }
 				}else{
 					showToast("Amount must be greater than zero");
 				}
 			}else{
 				showToast("Invalid Amount");
 			}
 		}
	}
	public void callsNotificationSetter(){
			notifyService.cancelAll();
	 		int DEBT_NOTIFICATION_ID;
	 		List<ClassDebt> debts = db.getAllDebt();
	 		for (ClassDebt dc : debts) {
	 			if(!TextUtils.isEmpty(dc.getDebtDueParse().toString())){
		 			DEBT_NOTIFICATION_ID = dc.getID();
		 			String[] DATETIME_PARSED  = dc.getDebtDueParse().split("\\.");
			    	Calendar d = Calendar.getInstance();
			    	int year_n,month_n,day_n;
			    	year_n = Integer.parseInt(DATETIME_PARSED[0]);
			    	month_n = Integer.parseInt(DATETIME_PARSED[1]);
			    	day_n = Integer.parseInt(DATETIME_PARSED[2]);
		 			if(DATETIME_PARSED.length > 3){
				    	d.set(year_n, month_n, day_n);
				    	d.set(Calendar.HOUR_OF_DAY, Integer.parseInt(DATETIME_PARSED[3]));
				    	d.set(Calendar.MINUTE, Integer.parseInt(DATETIME_PARSED[4]));
				    	d.set(Calendar.SECOND, 0);
		 			}else{
				    	d.set(year_n, month_n, day_n);
				    	d.set(Calendar.HOUR_OF_DAY, 0);
				    	d.set(Calendar.MINUTE, 0);
				    	d.set(Calendar.SECOND, 0);
		 			}
			 		scheduleClient.setAlarmForNotification(d, DEBT_NOTIFICATION_ID);
	 			}
	 		}
	}
	 public Bitmap getRoundedShape(Bitmap bitmap) {
		 int targetWidth = bitmap.getWidth();
		 int targetHeight = bitmap.getHeight();
		 if(targetWidth < 96 || targetHeight < 96){
			 targetWidth = 96;
			 targetHeight = 96;
		 }
		 Bitmap output = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, targetWidth, targetHeight);
			final RectF rectF = new RectF(rect);

			paint.setAntiAlias(true);
			paint.setDither(true);
			paint.setFilterBitmap(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawOval(rectF, paint);

			paint.setColor(Color.BLUE);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth((float) 4);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);

			return output;
		 }
	public Bitmap getCroppedBitmap(Bitmap bitmap) {
		    Bitmap output = Bitmap.createBitmap(110, 110, Config.ARGB_8888);
		    Canvas canvas = new Canvas(output);
		    final int color = 0xff424242;
		    final Paint paint = new Paint();
		    final Rect rect = new Rect(0, 0, 300, 300);
	
		    paint.setAntiAlias(true);
		    canvas.drawARGB(0, 0, 0, 0);
		    paint.setColor(color);
		    paint.setStrokeWidth(0);
		    canvas.drawCircle(50, 50, 50, paint);
		    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		    canvas.drawBitmap(bitmap, rect, rect, paint);
		    return output;
	}
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
		    bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, 96, 96);
		final RectF rectF = new RectF(rect);
		final float roundPx = 50;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
		}
	public Bitmap getPhoto(String phoneNumber) {
	    Uri phoneUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
	    Uri photoUri = null;
	    ContentResolver cr = this.getContentResolver();
	    Cursor contact = cr.query(phoneUri,
	            new String[] { ContactsContract.Contacts._ID}, null, null, null);

	    if (contact.moveToFirst()) {
	        long userId = contact.getLong(contact.getColumnIndex(ContactsContract.Contacts._ID));
	        photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, userId);

	    }
	    else {
	        Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.user);
	        return defaultPhoto;
	    }
	    
	    if (photoUri != null) {
	        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, photoUri);
	        if (input != null) {
	            return getRoundedShape(BitmapFactory.decodeStream(input));
	        }
	    } else {
	        Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.user);
	        return defaultPhoto;
	    }
	    Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.user);
	    return defaultPhoto;
	}
	private void setCurrentTime(){
			final Calendar c = Calendar.getInstance();
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);
			timePicker.setCurrentHour(hour);
			timePicker.setCurrentMinute(minute);
	}
	private void setCurrentDate(){
		final Calendar c = Calendar.getInstance();
		yr = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		btnDate.setHint(new StringBuilder().append(MONTHS[month])
				   .append(" ").append(day).append(", ").append(yr)
				   .append(" "));
	}
	// Date Picker Listener
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
	// Date Picker
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener()
 	{
 		public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour)
 			{
 				hour = hourOfDay;
 				minute = minuteOfHour;
 				PICKER_TIME_VALUE = hour + "." + minute;
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
			PICKER_DATE_VALUE = year + "." + monthOfYear + "." + dayOfMonth;
			SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
		    Date date = new Date(yr, month, day-1);
		    String dayOfWeek = simpledateformat.format(date).substring(0,3);
			btnDate.setText(new StringBuilder().append(dayOfWeek + ", ").append(MONTHS[month])
					   .append(" ").append(day).append(", ").append(yr)
					   .append(" "));
		}
 	};
	//Check Text Fields if empty
 	private boolean isEmpty(EditText txt) {
 	    if (txt.getText().toString().trim().length() > 0) {
 	        return false;
 	    } else {
 	        return true;
 	    }
 	}
 	//Actions of ActionBar Items
	@Override
 	public boolean onOptionsItemSelected(MenuItem item)
	{
	    switch (item.getItemId())
	    {
	    case android.R.id.home:
	    	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	 		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
	 		startActivity(intent);
	 		finish();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	private void vibrateMe(){
		 Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		 v.vibrate(200);
	}
 	public void showToast(String msg){
 		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
 	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_debt, menu);
		return true;
	}
	@Override
	public boolean onNavigationItemSelected(int position, long arg1) {
		switch(position){
			case 0:
				lblName.setText("DEBTEE'S NAME");
				DEBT_TYPE = 0;
				break;
			case 1:
				lblName.setText("DEBTOR'S NAME");
				DEBT_TYPE = 1;
				break;
		}
		return false;
	}
 	public void slideIt(int opt){
 		if (opt != 0){
 			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
 		}
 	}
}