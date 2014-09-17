package com.ajdeguzman.debtnote;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MenuAbout extends Fragment {
	
	public MenuAbout(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
       
        return rootView;
    }
 	public void showToast(String msg){
 		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
 	}

}
