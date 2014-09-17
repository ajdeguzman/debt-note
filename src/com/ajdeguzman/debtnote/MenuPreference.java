package com.ajdeguzman.debtnote;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

public class MenuPreference extends PreferenceActivity {
	 ListPreference lstDefaultCurrency;
	 Preference prefDev, backupPreference, restorePreference;
	 String[]  entries = new String[78];
	 String[]  entryValues = new String[78];
	 ClassImportExportDB dbImportExport = new ClassImportExportDB(MenuPreference.this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.app_preference);
		bindCurrencies();
		lstDefaultCurrency = (ListPreference) findPreference("lstDefaultCurrency");
		prefDev = (Preference) findPreference("contactDevKey");
		backupPreference = (Preference) findPreference("backupPreference");
		restorePreference = (Preference) findPreference("restorePreference");
		PreferenceManager.setDefaultValues(this, R.xml.app_preference, false);
		lstDefaultCurrency.setEntryValues(entryValues);
		lstDefaultCurrency.setEntries(entries);
		prefDev.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				if(isMailClientPresent(getApplicationContext())){
					Intent email = new Intent(Intent.ACTION_SEND);
					email.putExtra(Intent.EXTRA_EMAIL, new String[]{"aljohndeguzman@gmail.com"});	
					email.setType("message/rfc822");
					startActivity(Intent.createChooser(email, "Choose an Email Client"));	
            	}else{
            		showToast("No apps can perform this client");
            	}
				return false;
			}
        });
		backupPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference arg0) {
					dbImportExport.exportDB();
				return false;
			}
		});
		restorePreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference arg0) {
					dbImportExport.importDB();
				return false;
			}
		});
	}
	public static boolean isMailClientPresent(Context context){
	    Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.setType("text/html");
	    final PackageManager packageManager = context.getPackageManager();
	    List<ResolveInfo> list = packageManager.queryIntentActivities(intent, 0);
	    if(list.size() == 0)
	        return false;
	    else 
	        return true;
	}
	private void bindCurrencies(){
	       for (int i = 0; i < entries.length; i++) {
	        	 entries[i] = "(" + new ClassCurrency().getSymbols(i) + ") " + new ClassCurrency(String.valueOf(i)).toString();
	        	 entryValues[i] = String.valueOf(i);
	        }
	}
	/*private void bindCurrencies(){
		 ArrayList<String> currencys = new ArrayList<String>();
	        Locale[] locs = Locale.getAvailableLocales();
	        int j = 0;
	        for(Locale loc : locs) {
	        	j = j + 1;
	            try {
	                String val=Currency.getInstance(loc).getCurrencyCode();
	                if(!currencys.contains(val))
	                    currencys.add(val);
	            } catch(Exception exc)
	            {
	                // Locale not found
	            }
	            Collections.sort(currencys);
	        }
	        for (int i = 0; i < currencys.size(); i++) {
	        	 Currency c  = Currency.getInstance(currencys.get(i));
	        	 entries[i] = "(" + c.getSymbol().toString() + ") " + new ClassCurrency(String.valueOf(i)).toString();
	        	 entryValues[i] = String.valueOf(i);
	        }
	        
	}*/
 	public void showToast(String msg){
 		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
 	}
	@SuppressLint("InlinedApi")
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
}
