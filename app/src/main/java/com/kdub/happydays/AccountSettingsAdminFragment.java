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
import android.view.accessibility.AccessibilityManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AccountSettingsAdminFragment extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_account_settings_admin, container, false);

    RelativeLayout profileButton = view.findViewById(R.id.profile_button_layout_admin);
    RelativeLayout aboutButton = view.findViewById(R.id.about_button_layout_admin);
    RelativeLayout logOutButton = view.findViewById(R.id.logout_button_layout_admin);
    RelativeLayout manageUsersButton = view.findViewById(R.id.manage_users_button_layout_admin);

    profileButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        fragmentSwitch(new ProfileFragment());
      }
    });

    manageUsersButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO: write the action to see all users
        Toast.makeText(getContext(), "not yet implemented", Toast.LENGTH_SHORT).show();
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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
      }
    });

    return view;
  }

  private void fragmentSwitch(Fragment switchHere) {
    FragmentTransaction fragment = getActivity().getSupportFragmentManager().beginTransaction();
    fragment.replace(R.id.frame_layout_admin, switchHere);
    fragment.commit();
  }
}