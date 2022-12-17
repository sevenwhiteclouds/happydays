package com.kdub.happydays;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ManageUsersAdminFragment extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_manage_users_admin, container, false);

    Button back = view.findViewById(R.id.admin_back_button_manage_user);

    back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FragmentTransaction fragment = getActivity().getSupportFragmentManager().beginTransaction();
        fragment.replace(R.id.frame_layout_admin, new AccountSettingsAdminFragment());
        fragment.commit();
      }
    });

    RecyclerView recyclerView = view.findViewById(R.id.admin_recyler_view_manage_user);

    ManageUsersAdapterAdmin adapter = new ManageUsersAdapterAdmin(getContext());

    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(adapter);

    return view;
  }
}