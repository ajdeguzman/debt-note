package com.ajdeguzman.debtnote.service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ajdeguzman.debtnote.service.NotifyService;

/**
 * Set an alarm for the date passed into the constructor
 * When the alarm is raised it will start the NotifyService
 * 
 * This uses the android build in alarm manager *NOTE* if the phone is turned off this alarm will be cancelled
 * 
 * This will run on it's own thread.
 * 
 * @author paul.blundell
 */
public class AlarmTask implements Runnable{
	private final Calendar date;
	private final AlarmManager am;
	private final Context context;
	private final int id;

	public AlarmTask(Context context, Calendar date, int id) {
		this.context = context;
		this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		this.date = date;
		this.id = id;
	}
	
	@Override
	public void run() {
		// Request to start are service when the alarm date is upon us
		// We don't start an activity as we just want to pop up a notification into the system bar not a full activity
		Intent intent = new Intent(context, NotifyService.class);
		intent.putExtra(NotifyService.INTENT_NOTIFY, true);
		intent.putExtra("id", this.id);
		PendingIntent pendingIntent = PendingIntent.getService(context, this.id, intent, 0);
		// Sets an alarm - note this alarm will be lost if the phone is turned off and on again
		am.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
	}
}
