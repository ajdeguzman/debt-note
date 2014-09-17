package com.ajdeguzman.debtnote;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuHistory extends Fragment {
	DebtDatabaseHandler db;
    private ActionMode mActionMode;
	ListView lstHistory; 
	TextView txtEmpty;
    String searchQueryName, searchQueryPhone, searchQueryId;
	String pref_curr;
	int yr, month, day, weekday;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        lstHistory = (ListView)rootView.findViewById(R.id.listview_history);
        txtEmpty = (TextView)rootView.findViewById(R.id.txtEmpty);
		db = new DebtDatabaseHandler(getActivity());
		getPreferencesValue();
		populateHistory();
		Calendar today = Calendar.getInstance();
		yr = today.get(Calendar.YEAR);
		month = today.get(Calendar.MONTH);
		day = today.get(Calendar.DAY_OF_MONTH);
		weekday = today.get(Calendar.DAY_OF_WEEK);
		lstHistory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> map = (HashMap<String, String>) lstHistory.getItemAtPosition(position);
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
        return rootView;
    }
	private void showOptions(){
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setCancelable(true);
        adb.setTitle("Archive Options");
        adb.setItems(R.array.history_options,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                        	case 0:
                        		deleteHistory();
                        		break;
                        }
                    }
                });
        adb.show();
	}
	private void getPreferencesValue(){
 		SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
 		pref_curr = appPrefs.getString("lstDefaultCurrency", "53");
	}
 	public void showToast(String msg){
 		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
 	}
 	private void vibrateMe(){
		 Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		 v.vibrate(100);
	}
 	private void populateHistory(){
 		 List<ClassHistory> history = db.getAllHistoryWhere(0);  
		 List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		 String currency = new ClassCurrency().getSymbols(Integer.parseInt(pref_curr));
		 for (ClassHistory hist : history) {
			 Map<String, Object> datum = new HashMap<String, Object>();
				datum.put("picture", hist.getPicture());
			    if(!TextUtils.isEmpty(hist.getDesc())){
			    	if(currency.length() == 1){
			    		datum.put("initial", hist.getDesc() + " (Initial Amount: " + currency + " " +  hist.getInitial() + ")");
			    	}else{
			    		datum.put("initial", hist.getDesc() + " (Initial Amount: " + hist.getInitial() + " " + currency + ")");
			    	}
			    }else{
			    	if(currency.length() == 1){
			    		datum.put("initial", "No Description" + " (Initial Amount: " + currency + " " +  hist.getInitial() + ")");
			    	}else{
			    		datum.put("initial", "No Description" + " (Initial Amount: " + hist.getInitial() + " " + currency + ")");
			    	}
			    }
			    datum.put("name", hist.getPerson());
			    datum.put("date_created", "Created: " + hist.getCreated());
			    datum.put("date_ended", "Ended: " + hist.getEnded());
			    datum.put("id",hist.getHistID());
			    data.add(datum);
		 }	
		 ExtendedSimpleAdapter historyAdapter = new ExtendedSimpleAdapter (getActivity(), data, R.layout.history_layout,
	                new String[] {"picture", "initial", "name", "date_created", "date_ended", "id"},
	                new int[] {R.id.img,  R.id.initial, R.id.name, R.id.txtDebtCreated, R.id.txtDebtEnded, R.id.id});
		 	lstHistory.setAdapter(historyAdapter);
		 	lstHistory.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    	if(historyAdapter.getCount() == 0){
				txtEmpty.setText("List is empty.");
			}else{
				txtEmpty.setText("");
			}
 	}
	private void deleteHistory(){
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle("Delete this item?");
		adb.setMessage("This item will be removed from archive");
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				db.deleteHistoryByHistId(Integer.parseInt(searchQueryId));
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					mActionMode.finish();
				}
				populateHistory();
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
 	class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
 	       	switch (item.getItemId()) {
 		            case R.id.item_delete:
 		            	deleteHistory();
 		            	break;
 	       	}
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.history_contextual_menu, menu);
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

}
