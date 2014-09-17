package com.ajdeguzman.debtnote;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuPeople extends Fragment {

	DebtDatabaseHandler db;
	TextView txtEmpty, txtOwedFrom, txtOwedBy;
    private ActionMode mActionMode;
    String searchQueryName, searchQueryPhone, searchQueryEmail, searchQueryId;
	ListView list; 
	String pref_curr;
	Button notifCount;
	int mNotifCount = 0;  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_people, container, false);
        list = (ListView)rootView.findViewById(R.id.listview_people);
        txtEmpty = (TextView)rootView.findViewById(R.id.txtEmpty);
        txtOwedFrom = (TextView)rootView.findViewById(R.id.txtOwedFrom);
        txtOwedBy = (TextView)rootView.findViewById(R.id.txtOwedBy);
		db = new DebtDatabaseHandler(getActivity());
		getPreferencesValue();
        populateListPeople();
		setHasOptionsMenu(true);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> map = (HashMap<String, String>) list.getItemAtPosition(position);
				searchQueryName = map.get("name");
				searchQueryPhone = map.get("phone");
				searchQueryEmail = map.get("email");
				searchQueryId = String.valueOf(map.get("id"));
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					mActionMode = getActivity().startActionMode(new ActionBarCallBack());
					vibrateMe();
				}else{
					showOptions();
				}
				return true;
			}
        	
        });
        list.setOnItemClickListener(new OnItemClickListener() {
			@Override
        	@SuppressWarnings("unchecked")
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        	}
         });
        return rootView;
    }

	private void vibrateMe(){
		 Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		 v.vibrate(100);
	}
	@Override
	public void onStart(){
		super.onStart();
		setNotifCount(db.getContactCount());
	}
	private void getPreferencesValue(){
 		SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
 		pref_curr = appPrefs.getString("lstDefaultCurrency", "53");
	}	
	private void populateListPeople(){
		 DecimalFormat df = new DecimalFormat("#,###,###.00");
		 List<ClassContact> contacts = db.getAllContact();  
		 List<ClassDebt> contactsOwedFrom, contactsOwedBy;
		 String currency = new ClassCurrency().getSymbols(Integer.parseInt(pref_curr));
		 List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			for (ClassContact cn : contacts) {
			    Map<String, Object> datum = new HashMap<String, Object>();
				datum.put("picture", cn.getPicture());
			    datum.put("name", cn.getName());
			    datum.put("phone", cn.getPhone());
			    datum.put("email", cn.getEmail());
			    datum.put("id", cn.getID());
			    
					contactsOwedFrom = db.getAllDebtWhereAndWho(0, cn.getName());
					float countFrom = 0;
					for (ClassDebt debtFrom : contactsOwedFrom) {
						countFrom = countFrom + Float.parseFloat(debtFrom.getDebtAmount().replace(",",""));
					}	
					contactsOwedBy = db.getAllDebtWhereAndWho(1, cn.getName());
					float countBy = 0;  
					for (ClassDebt debtBy : contactsOwedBy) {
						countBy = countBy + Float.parseFloat(debtBy.getDebtAmount().replace(",",""));
					}
			    if(currency.length() == 1){
					datum.put("from", (countFrom != .00 ? "\u21B6 " + currency + " " + df.format(countFrom) : "\u21B6 " + currency + " " +  "0.00"));
					datum.put("by", (countBy != .00 ?  "\u21B7 " + currency + " " + df.format(countBy) :  "\u21B7 " + currency + " " +  "0.00"));
			    }else{
					datum.put("from",(countFrom != .00 ? "\u21B6 " + df.format(countFrom) + " " + currency :  "\u21B6 " + "0.00"  + " " +  currency));
					datum.put("by", (countBy != .00 ? "\u21B7 " + df.format(countBy) + " " + currency :  "\u21B7 " + "0.00"  + " " +  currency));
			    }
			    data.add(datum);
			}
	        ExtendedSimpleAdapter mSchedule = new ExtendedSimpleAdapter (getActivity(), data, R.layout.listview_layout,
	                new String[] {"picture", "name", "phone", "id", "by", "from"},
	                new int[] {R.id.img,  R.id.name, R.id.phone, R.id.people_id, R.id.txtOwedBy, R.id.txtOwedFrom});
	        list.setAdapter(mSchedule);
	    	if(mSchedule.getCount() == 0){
				txtEmpty.setText("List is empty.");
			}else{
				txtEmpty.setText("");
			}
	}
	private void setNotifCount(int count){
	    mNotifCount = count;
		getActivity().supportInvalidateOptionsMenu();
	}
	private void showOptions(){
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
       adb.setCancelable(true);
       adb.setTitle("People Options");
       adb.setItems(R.array.people_options,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                        	case 0:
                        		callPerson();
                        		break;
                        	case 1:
                        		emailPerson();
                        		break;
                        	case 2:
                        		deletePerson();
                        		break;
                        }
                    }
                });
        adb.show();
	}
	
	private void deletePerson(){
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle("Close All Debt");
		adb.setMessage("Are you sure you want to close all debts with " + searchQueryName + "?");
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				List<ClassDebt> debt = db.getAllDebtByName(searchQueryName); 
				for(ClassDebt dt : debt) {
					db.deleteHistoryById(dt.getID());
					db.deletePaymentById(dt.getID());
					db.deleteDebtById(String.valueOf(dt.getID()));
				}
				db.deleteContact(searchQueryName);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					mActionMode.finish();
				}
				populateListPeople();
				setNotifCount(db.getContactCount());
			}
		});
		adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		adb.show();
	}
	private void emailPerson(){
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[]{searchQueryEmail});		  
		email.putExtra(Intent.EXTRA_SUBJECT, "Debt Note Reminder");
		email.putExtra(Intent.EXTRA_TEXT,
					Html.fromHtml(new StringBuilder()
				    .append("<p>Good Day!</p>")
				    .append("<p>This is to remind you that you owe me.</p><br>")
				    .append("<p>- Debt Note</p>")
				    .toString())
				    );
		email.setType("message/rfc822");
		startActivity(Intent.createChooser(email, "Choose an Email Client"));
	}
	private void callPerson(){
		if(!TextUtils.isEmpty(searchQueryPhone)){
            String num = searchQueryPhone.toString().replaceAll("\\s+","");
            if(num.charAt(0) == '+'){
            	num = num.replace("+63", "0");
            }
 			 startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num)));
		}else{
			showToast("No contact number");
		}
	}
	class ActionBarCallBack implements ActionMode.Callback {
       @Override
       public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	       	switch (item.getItemId()) {
		            case R.id.item_delete:
		            	deletePerson();
		            	break;
		            case R.id.item_call:
		            	callPerson();
		            	break;
		            case R.id.item_email:
		            	emailPerson();
		            	break;
	       	}
           return false;
       }

       @Override
       public boolean onCreateActionMode(ActionMode mode, Menu menu) {
           mode.getMenuInflater().inflate(R.menu.people_contextual_menu, menu);
           return true;
       }

       @Override
       public void onDestroyActionMode(ActionMode mode) {
       }

       @Override
       public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
           mode.setTitle("Select action");
           return false;
       }

   }
 	@Override
 	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
 	   inflater.inflate(R.menu.people_count, menu);
 	   MenuItem item = menu.findItem(R.id.action_badge); 		
 	   MenuItemCompat.setActionView(item, R.layout.feed_update_count); 	
 	   notifCount = (Button) MenuItemCompat.getActionView(item);
	   notifCount.setText(mNotifCount+"");
 	   super.onCreateOptionsMenu(menu, inflater);
 	}
 	public void showToast(String msg){
 		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
 	}
}
