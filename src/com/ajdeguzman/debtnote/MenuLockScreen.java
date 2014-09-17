package com.ajdeguzman.debtnote;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class MenuLockScreen extends ActionBarActivity {
 	protected EditText pinCodeField1 = null;
 	protected EditText pinCodeField2 = null;
 	protected EditText pinCodeField3 = null;
 	protected EditText pinCodeField4 = null;
    protected InputFilter[] filters = null;
    protected TextView topMessage = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_lock_screen);
	}
}
