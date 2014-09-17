package com.ajdeguzman.debtnote;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuDashboard extends Fragment {
	DebtDatabaseHandler db;
	int activity_count;
	Button btnStart;
	TextView txtIOwe, txtOwesMe;
	String pref_curr;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		db = new DebtDatabaseHandler(getActivity());
		activity_count = db.getDebtCount();
		View rootView = null;
		if(activity_count != 0){
				rootView = inflater.inflate(R.layout.fragment_home, container, false);
				txtIOwe = (TextView)rootView.findViewById(R.id.txtIOwe);
				txtOwesMe = (TextView)rootView.findViewById(R.id.txtOwesMe);
				getPreferencesValue();
				populateDashBoard();
		}else{
			rootView = inflater.inflate(R.layout.start_create, container, false);
			btnStart = (Button)rootView.findViewById(R.id.btnStart);
			btnStart.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					startActivity(new Intent(getActivity(), AddDebt.class));
				}
				
			});
		}
        return rootView;
    }
	private void getPreferencesValue(){
 		SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
 		pref_curr = appPrefs.getString("lstDefaultCurrency", "53");
	}	
	private void populateDashBoard(){
				float totalOwedCount = 0, totalOwesCount = 0;
				List<ClassDebt> owedList = db.getAllDebtWhere(0);
				List<ClassDebt> owesMeList = db.getAllDebtWhere(1);
				String currency = null;
				DecimalFormat df = new DecimalFormat("#,###,###.00");
				
				for (ClassDebt dc : owedList) {
					currency = new ClassCurrency().getSymbols(Integer.parseInt(pref_curr));
					totalOwedCount = totalOwedCount +  Float.parseFloat(dc.getDebtAmount().replace(",",""));
				}
					if((int)(totalOwedCount) != 0){
						txtIOwe.setText("TOTAL: " + currency + " " + df.format((int)totalOwedCount));
					}else{
						txtIOwe.setText("TOTAL: " + currency + " " + "0.00");
					}
				for (ClassDebt dc : owesMeList) {
					totalOwesCount = totalOwesCount +  Float.parseFloat(dc.getDebtAmount().replace(",",""));
				}
					if((int)(totalOwesCount) != 0){
						txtOwesMe.setText("TOTAL: " + currency + " " + df.format((int)totalOwesCount));
					}else{
						txtOwesMe.setText("TOTAL: " + currency + " " + "0.00");
					}
			
	}
}
