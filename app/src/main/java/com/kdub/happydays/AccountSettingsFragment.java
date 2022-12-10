package com.kdub.happydays;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class AccountSettingsFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_account_settings, container, false);

    RelativeLayout profileButton = view.findViewById(R.id.profile_button_layout);
    RelativeLayout aboutButton = view.findViewById(R.id.about_button_layout);
    RelativeLayout logOutButton = view.findViewById(R.id.logout_button_layout);
    RelativeLayout deleteAccountButton = view.findViewById(R.id.delete_account_button_layout);

    profileButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        fragmentSwitch(new ProfileFragment());
      }
    });

    aboutButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        fragmentSwitch(new AboutFragment());
      }
    });

    logOutButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // code to logout here
        SharedPreferences mPreferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mPreferences.edit();
        mEditor.clear();
        mEditor.commit();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
      }
    });

    deleteAccountButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // here to delete account code

      }
    });

    return view;
  }

  private void fragmentSwitch(Fragment switchHere) {
    FragmentTransaction fragment = getActivity().getSupportFragmentManager().beginTransaction();
    fragment.replace(R.id.frame_layout, switchHere);
    fragment.commit();
  }
}