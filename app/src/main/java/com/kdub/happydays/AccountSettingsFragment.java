package com.kdub.happydays;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.kdub.happydays.databinding.ActivityHomescreenBinding;
import com.kdub.happydays.databinding.ActivityMainBinding;

public class AccountSettingsFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_account_settings, container, false);

    RelativeLayout aboutButton = view.findViewById(R.id.about_button_layout);
    RelativeLayout logOutButton = view.findViewById(R.id.logout_button_layout);

    aboutButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FragmentTransaction fr = getActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.frame_layout, new AboutFragment());
        fr.commit();
      }
    });

    logOutButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // code to logout here
      }
    });

    return view;
  }
}