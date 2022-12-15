package com.kdub.happydays;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;

public class ProfileFragment extends Fragment {

  User user = null;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_profile, container, false);

    HappyDAO mLoginDao = Room.databaseBuilder(getActivity(), AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().happyDAO();

    EditText firstName = view.findViewById(R.id.show_change_first_name);
    EditText lastName = view.findViewById(R.id.show_change_last_name);
    EditText passWord = view.findViewById(R.id.show_change_pass);

    SharedPreferences preferences = getActivity().getSharedPreferences("session", MODE_PRIVATE);

    int userId = preferences.getInt("userId", 0);

    user = mLoginDao.getUserByUserId(userId);

    Button back = view.findViewById(R.id.back_button_profile);
    Button saveChanges = view.findViewById(R.id.save_button_profile);

    firstName.setText(user.getFirstName());
    lastName.setText(user.getLastName());
    passWord.setText(user.getPassword());

    back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FragmentTransaction fragment = getActivity().getSupportFragmentManager().beginTransaction();
        fragment.replace(R.id.frame_layout, new AccountSettingsFragment());
        fragment.commit();
      }
    });

    saveChanges.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        EditText firstName = getView().findViewById(R.id.show_change_first_name);
        EditText lastName = getView().findViewById(R.id.show_change_last_name);
        EditText passWord = getView().findViewById(R.id.show_change_pass);

        user.setFirstName(firstName.getText().toString());
        user.setLastName(lastName.getText().toString());
        user.setPassword(passWord.getText().toString());

        mLoginDao.update(user);

        Toast.makeText(getActivity(), "Happy Days! Changes have been saved. :)", Toast.LENGTH_SHORT).show();
        FragmentTransaction fragment = getActivity().getSupportFragmentManager().beginTransaction();
        fragment.replace(R.id.frame_layout, new AccountSettingsFragment());
        fragment.commit();
      }
    });

    return view;
  }
}