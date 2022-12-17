package com.kdub.happydays;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AboutFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_about, container, false);

    SharedPreferences preferences = getActivity().getSharedPreferences("session", MODE_PRIVATE);

    boolean isAdmin = preferences.getBoolean("isAdmin", false);

    Button back = view.findViewById(R.id.back_button_about);

    back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FragmentTransaction fragment = getActivity().getSupportFragmentManager().beginTransaction();
        if (isAdmin) {
          fragment.replace(R.id.frame_layout_admin, new AccountSettingsAdminFragment());
        }
        else {
          fragment.replace(R.id.frame_layout_normal, new AccountSettingsFragment());
        }
        fragment.commit();
      }
    });

    return view;
  }
}