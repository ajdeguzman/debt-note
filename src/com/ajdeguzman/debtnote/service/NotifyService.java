package com.ajdeguzman.debtnote.service;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ajdeguzman.debtnote.ClassCurrency;
import com.ajdeguzman.debtnote.ClassDebt;
import com.ajdeguzman.debtnote.DebtDatabaseHandler;
import com.ajdeguzman.debtnote.MainActivity;
import com.ajdeguzman.debtnote.R;

/**
 * This service is started when an Alarm has been raised
 * 
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 * 
 * @author paul.blundell
 */
public class NotifyService extends Service {
	
	public class ServiceBinder extends Binder {
		NotifyService getService() {
			return NotifyService.this;
		}
	}
	public NotifyService() {
	}
	// Name of an intent extra we can use to identify if this service was started to create a notification	
	public static final String INTENT_NOTIFY = "com.ajdeguzman.debtnote.service.INTENT_NOTIFY";
	public static int IDENTIFIER = 0;
	private NotificationManager mNM;
	String pref_curr;
	DebtDatabaseHandler db = new DebtDatabaseHandler(this);
	float DEBT_AMOUNT;
	@Override
	public void onCreate() {
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}
	private void getPreferencesValue(){
 		SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 		pref_curr = appPrefs.getString("lstDefaultCurrency", "53");
	}	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		IDENTIFIER = intent.getIntExtra("id", 0);
		if(intent.getBooleanExtra(INTENT_NOTIFY, false))
			showNotification();
		return START_NOT_STICKY;
	}
	public void cancelAll(){
		if(mNM != null){
			mNM.cancelAll();
		}
	}
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	private final IBinder mBinder = new ServiceBinder();
	private void showNotification() {
		getPreferencesValue();
		String currency = new ClassCurrency().getSymbols(Integer.parseInt(pref_curr));
		CharSequence title = "Debt Note";
		CharSequence text = "Debt Payment Due";	
		List<ClassDebt> debts = db.getDebt(IDENTIFIER);
 		for (ClassDebt dc : debts) {
 			title = dc.getDebtPerson() + "(" + currency + dc.getDebtAmount() + ")";
 			DEBT_AMOUNT = Float.parseFloat(dc.getDebtAmount().replace(",",""));
 		}
 		
		SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String pref_ringtone = appPrefs.getString("ringtonePreference", "DEFAULT_SOUND");
		boolean pref_status = appPrefs.getBoolean("chkNotification", true);
		Uri alarmSound = Uri.parse(pref_ringtone);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
		Notification notification = new Notification.Builder(this)
			.setContentTitle(text)
	        .setContentText(title)
	        .setSmallIcon(R.drawable.notif_logo)
	        .setContentIntent(contentIntent)
	        .setLights(Color.BLUE, 500, 500)
	        .setAutoCancel(true)
			.setVibrate(new long[] { 100, 250, 100, 250, 100, 250 })
	        .setSound(alarmSound).build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		if(pref_status && DEBT_AMOUNT != 0){
			mNM.notify(IDENTIFIER, notification);
		}
		stopSelf();
	}
}