package com.kdub.happydays;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;

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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
      }
    });

    deleteAccountButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // here to delete account code
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("Delete Account");
        builder.setMessage("You are about to delete your account. This process is irreversible. All account data will be lost.");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
          }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            TextView alertText = view.findViewById(R.id.delete_account_warning_text);

            Toast.makeText(getActivity(), "Account has been deleted.", Toast.LENGTH_SHORT).show();
            deleteAccount();
          }
        });

        builder.show();
      }
    });

    return view;
  }

  private void deleteAccount() {
    HappyDAO mHappyDao = Room.databaseBuilder(getActivity(), AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().happyDAO();

    SharedPreferences mPreferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
    SharedPreferences.Editor mEditor = mPreferences.edit();

    int userId = mPreferences.getInt("userId", 0);

    mEditor.clear();
    mEditor.commit();

    User user = mHappyDao.getUserByUserId(userId);

    mHappyDao.delete(user);

    Intent intent = new Intent(getActivity(), MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
  }

  private void fragmentSwitch(Fragment switchHere) {
    FragmentTransaction fragment = getActivity().getSupportFragmentManager().beginTransaction();
    fragment.replace(R.id.frame_layout, switchHere);
    fragment.commit();
  }
}