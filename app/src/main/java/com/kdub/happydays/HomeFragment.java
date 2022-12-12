package com.kdub.happydays;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.LoginDAO;
import java.util.Calendar;

public class HomeFragment extends Fragment {

  private View view = null;
  private SharedPreferences preferences = null;
  private LoginDAO mLoginDao = null;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_home, container, false);

    preferences = getActivity().getSharedPreferences("session", MODE_PRIVATE);
    mLoginDao = Room.databaseBuilder(getActivity(), AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().LoginDAO();

    setTimeOfDayHiMessage();


    // Inflate the layout for this fragment
    return view;
  }

  // TODO: fix the stuff missing that SuppressLint is not needed
  @SuppressLint("SetTextI18n")
  private void setTimeOfDayHiMessage() {
    Calendar calendar = Calendar.getInstance();
    int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

    TextView homeHeadingText = view.findViewById(R.id.home_heading_text);

    int userId = preferences.getInt("userId", 0);
    String firstName = mLoginDao.getUserByUserId(userId).getFirstName();
    firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);

    if (currentHour < 12) {
      homeHeadingText.setText("Good morning, " + firstName);
    }
    else if (currentHour < 18) {
      homeHeadingText.setText("Good afternoon, " + firstName);
    }
    else {
      homeHeadingText.setText("Good evening, " + firstName);
    }
  }
}