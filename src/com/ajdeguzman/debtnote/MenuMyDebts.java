package com.ajdeguzman.debtnote;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MenuMyDebts extends Fragment {

	DebtDatabaseHandler db;
	int ITEM_POSITION;
	private ActionMode mActionMode;
	LinearLayout layoutAbove;
	String searchQueryName, searchQueryPhone, searchQueryId;
	ListView listview_my_debts;
	TextView txtEmpty, txtTotal, txtCount;
	DateFormat formatter = null;
	Date convertedDate = null;
	MenuItem item;
	String pref_curr;
	Menu menu;
	MenuItem abs_sort;
	int SORT_OPTIONS_SELECTED = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_my_debts, container,
				false);
		db = new DebtDatabaseHandler(getActivity());
		listview_my_debts = (ListView) rootView
				.findViewById(R.id.listview_my_debts);
		layoutAbove = (LinearLayout) rootView.findViewById(R.id.layoutAbove);
		txtEmpty = (TextView) rootView.findViewById(R.id.txtEmpty);
		txtTotal = (TextView) rootView.findViewById(R.id.txtTotal);
		txtCount = (TextView) rootView.findViewById(R.id.txtCount);
		getPreferencesValue();
		populateListDebt();

		listview_my_debts
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@SuppressLint("NewApi")
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int position, long arg3) {
						@SuppressWarnings("unchecked")
						HashMap<String, String> map = (HashMap<String, String>) listview_my_debts
								.getItemAtPosition(position);
						ITEM_POSITION = position;
						searchQueryId = map.get("id");
						searchQueryName = map.get("name");
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							mActionMode = getActivity().startActionMode(
									new ActionBarCallBack());
							vibrateMe();
						} else {
							showOptions();
						}
						return true;
					}
				});
		listview_my_debts.setOnItemClickListener(new OnItemClickListener() {
			@Override
			@SuppressWarnings({ "unchecked", "null" })
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				HashMap<String, String> map = (HashMap<String, String>) listview_my_debts
						.getItemAtPosition(position);
				Intent i = null;
				i = new Intent(getActivity(), SingleDebt.class);
				Bundle bundle = new Bundle();
				bundle.putString("id", map.get("id"));
				bundle.putInt("type", 0);
				i.putExtras(bundle);
				startActivity(i);
			}
		});
		return rootView;
	}

	private void vibrateMe() {
		Vibrator v = (Vibrator) getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);
		v.vibrate(100);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (isDebtNotEmpty()) {
			setHasOptionsMenu(true);
		}
	}

	private void showOptions() {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setCancelable(true);
		adb.setItems(R.array.debts_options,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							editPerson();
							break;
						case 1:
							deleteDebt();
							break;
						}
					}
				});
		adb.show();
	}

	private boolean isDebtNotEmpty() {
		boolean empty = false;
		if (db.getDebtCountByType(0) > 0) {
			empty = true;
		}
		return empty;
	}

	private void getPreferencesValue() {
		SharedPreferences appPrefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		pref_curr = appPrefs.getString("lstDefaultCurrency", "53");
	}

	private void sortDebt(String by) {
		List<ClassDebt> debts = db.sortDebtBy(by, 0);
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (ClassDebt dc : debts) {
			String date = dc.getDebtDate();
			String currency = new ClassCurrency().getSymbols(Integer
					.parseInt(pref_curr));
			formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
			try {
				convertedDate = (Date) formatter.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Map<String, Object> datum = new HashMap<String, Object>();
			List<ClassContact> contacts = db.getContactByName(dc
					.getDebtPerson().toString());
			for (ClassContact cn : contacts)
				datum.put("picture", cn.getPicture());
			if (dc.getDebDesc().trim().length() != 0) {
				datum.put("description", dc.getDebDesc());
			} else {
				datum.put("description", "No Description");
			}
			datum.put("id", String.valueOf(dc.getID()));
			datum.put("name", dc.getDebtPerson());
			if (Float.parseFloat(dc.getDebtAmount().replace(",", "")) != 0) {
				if (currency.length() == 1) {
					datum.put("amount", currency + " " + dc.getDebtAmount());
				} else {
					datum.put("amount", dc.getDebtAmount() + " " + currency);
				}
			} else {
				datum.put("amount", "\u2713 Repaid");
			}
			datum.put("date", new PrettyDate(convertedDate).toString());
			if (!dc.getDebtDue().trim().isEmpty()) {
				datum.put(
						"due",
						"Due: "
								+ dc.getDebtDue().substring(4,
										dc.getDebtDue().length()));
			} else {
				datum.put("due", dc.getDebtDue());
			}
			data.add(datum);
		}
		ExtendedSimpleAdapter adapter = new ExtendedSimpleAdapter(
				getActivity(), data, R.layout.my_debts_layout, new String[] {
						"picture", "id", "description", "name", "amount",
						"date", "due" }, new int[] { R.id.img, R.id.debt_id,
						R.id.singleDescription, R.id.person, R.id.singleAmount,
						R.id.singleDueDate, R.id.singleDate });
		listview_my_debts.setAdapter(adapter);
		if (adapter.getCount() == 0) {
			txtEmpty.setText("List is empty.");
		} else {
			txtEmpty.setText("");
		}
	}

	private void showSortOptions() {
		AlertDialog.Builder bldr = new AlertDialog.Builder(getActivity());
		bldr.setTitle("Sort By");
		bldr.setCancelable(true);
		bldr.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		bldr.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		bldr.setSingleChoiceItems(R.array.sort_options, SORT_OPTIONS_SELECTED,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							SORT_OPTIONS_SELECTED = 0;
							sortDebt("debt_person");
							break;
						case 1:
							SORT_OPTIONS_SELECTED = 1;
							sortDebt("debt_date");
							break;
						case 2:
							SORT_OPTIONS_SELECTED = 2;
							sortDebt("debt_amount");
							break;
						case 3:
							SORT_OPTIONS_SELECTED = 3;
							sortDebt("debt_due");
							break;
						}
					}
				});
		final AlertDialog alt = bldr.create();
		alt.show();
	}

	private void editPerson() {
		HashMap<String, String> map = (HashMap<String, String>) listview_my_debts
				.getItemAtPosition(ITEM_POSITION);
		Intent i = null;
		i = new Intent(getActivity(), AddDebt.class);
		Bundle bundle = new Bundle();
		bundle.putString("id", map.get("id"));
		bundle.putInt("type", 0);
		i.putExtras(bundle);
		startActivity(i);
	}

	private void deleteDebt() {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle("Close this debt?");
		adb.setMessage("This debt item will be closed");
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				db.deleteHistoryById(Integer.parseInt(searchQueryId));
				db.deletePaymentById(Integer.parseInt(searchQueryId));
				db.deleteDebtById(searchQueryId);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					mActionMode.finish();
				}
				populateListDebt();
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

	private void populateListDebt() {
		List<ClassDebt> debts = db.getAllDebtWhere(0);
		int count = 0;
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		float totalCount = 0;
		String currency = null;
		DecimalFormat df = new DecimalFormat("#,###,###.00");
		for (ClassDebt dc : debts) {
			String date = dc.getDebtDate();
			currency = new ClassCurrency().getSymbols(Integer
					.parseInt(pref_curr));
			formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
			try {
				convertedDate = (Date) formatter.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Map<String, Object> datum = new HashMap<String, Object>();
			List<ClassContact> contacts = db.getContactByName(dc
					.getDebtPerson().toString());
			for (ClassContact cn : contacts)
				datum.put("picture", cn.getPicture());
			if (dc.getDebDesc().trim().length() != 0) {
				datum.put("description", dc.getDebDesc());
			} else {
				datum.put("description", "No Description");
			}
			datum.put("id", String.valueOf(dc.getID()));
			datum.put("name", dc.getDebtPerson());
			if (Float.parseFloat(dc.getDebtAmount().replace(",", "")) != 0) {
				if (currency.length() == 1) {
					datum.put("amount", currency + " " + dc.getDebtAmount());
				} else {
					datum.put("amount", dc.getDebtAmount() + " " + currency);
				}
			} else {
				datum.put("amount", "\u2713 Repaid");
			}
			totalCount = totalCount
					+ Float.parseFloat(dc.getDebtAmount().replace(",", ""));
			datum.put("date", new PrettyDate(convertedDate).toString());
			if (dc.getDebtDue().trim().length() != 0) {
				datum.put(
						"due",
						"Due: "
								+ dc.getDebtDue().substring(4,
										dc.getDebtDue().length()));
			} else {
				datum.put("due", dc.getDebtDue());
			}
			data.add(datum);
			count = count + 1;
		}
		if ((int) (totalCount) != 0) {
			layoutAbove.setVisibility(View.VISIBLE);
			txtTotal.setText("Total: " + currency + " " + df.format(totalCount));
			txtCount.setText(String.valueOf(count));
		} else {
			layoutAbove.setVisibility(View.GONE);
		}
		ExtendedSimpleAdapter adapter = new ExtendedSimpleAdapter(
				getActivity(), data, R.layout.my_debts_layout, new String[] {
						"picture", "id", "description", "name", "amount",
						"date", "due" }, new int[] { R.id.img, R.id.debt_id,
						R.id.singleDescription, R.id.person, R.id.singleAmount,
						R.id.singleDueDate, R.id.singleDate });
		listview_my_debts.setAdapter(adapter);
		if (adapter.getCount() == 0) {
			txtEmpty.setText("List is empty.");
		} else {
			txtEmpty.setText("");
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_sort:
			if (isDebtNotEmpty()) {
				showSortOptions();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	class ActionBarCallBack implements ActionMode.Callback {
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.item_edit:
				editPerson();
				break;
			case R.id.item_delete:
				deleteDebt();
				break;
			}
			return false;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.getMenuInflater().inflate(R.menu.debt_contextual_menu, menu);
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

	public void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.add, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
}
