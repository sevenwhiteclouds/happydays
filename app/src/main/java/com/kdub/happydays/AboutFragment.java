package com.kdub.happydays;

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
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_about, container, false);

    Button back = view.findViewById(R.id.back_button_about);

    back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FragmentTransaction fragment = getActivity().getSupportFragmentManager().beginTransaction();
        fragment.replace(R.id.frame_layout_admin, new AccountSettingsFragment());
        fragment.commit();
      }
    });

    return view;
  }
}