package com.ajdeguzman.debtnote;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance;

public class AddDebtActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

    private Toolbar mToolbar;
    EditText txtDebtAmount, txtMobileNo, txtDescription, txtEmail;
    AutoCompleteTextView txtPerson;
    ArrayList<Map<String, String>> mPeopleList;
    LinearLayout layoutViewMore, layoutViewMoreEmail;
    RelativeLayout layoutAddMore;
    TextView lblName, lblCurrency, imgViewMore;
    Button btnTime, btnDate;
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
    DatePicker datePicker;
    int hour, minute;
    String name = null;
    int mYear, mMonth, mDay, mWeekday;
    String currentDate, currentTime;
    static final int TIME_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID = 1;
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private final static String[] DAYS_OF_WEEK = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


    private CoordinatorLayout layoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_debt);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setupActionBar();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Realm realm = Realm.getInstance(
                new RealmConfiguration.Builder(this)
                        .name("debt-note")
                        .build()
        );

        txtPerson           = (AutoCompleteTextView) findViewById(R.id.txtPerson);
        txtMobileNo         = (EditText) findViewById(R.id.txtMobileNo);
        txtEmail            = (EditText) findViewById(R.id.txtEmail);
        txtDebtAmount       = (EditText) findViewById(R.id.txtDebtAmount);
        txtDescription      = (EditText) findViewById(R.id.txtDescription);
        lblCurrency         = (TextView) findViewById(R.id.lblCurrency);
        lblName             = (TextView) findViewById(R.id.lblName);
        layoutViewMore      = (LinearLayout) findViewById(R.id.layoutViewMore);
        layoutViewMoreEmail = (LinearLayout) findViewById(R.id.layoutViewMoreEmail);
        datePicker          = (DatePicker) findViewById(R.id.datePicker);
        btnTime             = (Button) findViewById(R.id.btnTime);
        btnDate             = (Button) findViewById(R.id.btnDate);
        layoutRoot          = (CoordinatorLayout) findViewById(R.id.layoutRoot);

        setCurrentDate();
        setCurrentTime();

        Calendar today      = Calendar.getInstance();
        mYear               = today.get(Calendar.YEAR);
        mMonth              = today.get(Calendar.MONTH);
        mDay                = today.get(Calendar.DAY_OF_MONTH);
        mWeekday            = today.get(Calendar.DAY_OF_WEEK);

        txtPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        txtPerson.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (TextUtils.isEmpty(txtPerson.getText())) {
                    txtPerson.showDropDown();
                }
                return false;
            }
        });
        txtPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = newInstance(AddDebtActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMaxDate(now);
                dpd.setYearRange(dpd.getMinYear(), now.get(Calendar.YEAR));
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        AddDebtActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });
    }

    private void setupActionBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setCurrentTime() {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
    }

    private void setCurrentDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        btnDate.setHint(new StringBuilder().append(MONTHS[mMonth])
                .append(" ").append(mDay).append(", ").append(mYear)
                .append(" "));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String arrMonthNames[] = getResources().getStringArray(R.array.arr_string_months);
        btnDate.setText(new StringBuilder().append(arrMonthNames[monthOfYear]).append(" ").append(dayOfMonth).append(", ").append(year));

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String time = hourString+"h"+minuteString+"m";
        btnTime.setText(time);
    }
    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

        if(tpd != null) tpd.setOnTimeSetListener(this);
        if(dpd != null) dpd.setOnDateSetListener(this);
    }
    /*
    Onclick methods()
     */
    public void clickBtnSaveDebt(View v){

        if(Utils.isEmpty(txtPerson)){
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            findViewById(R.id.txtPerson).startAnimation(shake);
            Utils.showSnackBar(R.string.message_empty_fields, layoutRoot, Toast.LENGTH_SHORT);
        }
        if(Utils.isEmpty(txtDebtAmount)){
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            findViewById(R.id.txtDebtAmount).startAnimation(shake);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
