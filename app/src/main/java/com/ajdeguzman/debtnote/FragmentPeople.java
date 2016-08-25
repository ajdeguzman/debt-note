package com.ajdeguzman.debtnote;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class FragmentPeople extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        final View view = inflater.inflate(R.layout.fragment_layout_archive, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("People");
        Button buttonChangeText = (Button) view.findViewById(R.id.buttonFragmentInbox);
        final TextView textViewInboxFragment = (TextView) view.findViewById(R.id.textViewInboxFragment);


        return view;
    }

}