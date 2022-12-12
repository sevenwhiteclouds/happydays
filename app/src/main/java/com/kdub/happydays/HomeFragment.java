package com.kdub.happydays;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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
  private TextView button1 = null;
  private TextView button2 = null;
  private TextView button3 = null;
  private TextView button4 = null;
  private TextView button5 = null;
  private TextView button6 = null;
  private TextView button7 = null;
  private TextView button8 = null;
  private TextView button9 = null;
  private TextView button10 = null;
  private TextView button11 = null;
  private TextView button12 = null;
  private int[] categoryIds = new int[12];

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_home, container, false);

    preferences = getActivity().getSharedPreferences("session", MODE_PRIVATE);
    mLoginDao = Room.databaseBuilder(getActivity(), AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().LoginDAO();

    setTimeOfDayHiMessage();

    categoryIds[0] = R.id.category_button_1;
    categoryIds[1] = R.id.category_button_2;
    categoryIds[2] = R.id.category_button_3;
    categoryIds[3] = R.id.category_button_4;
    categoryIds[4] = R.id.category_button_5;
    categoryIds[5] = R.id.category_button_6;
    categoryIds[6] = R.id.category_button_7;
    categoryIds[7] = R.id.category_button_8;
    categoryIds[8] = R.id.category_button_9;
    categoryIds[9] = R.id.category_button_10;
    categoryIds[10] = R.id.category_button_11;
    categoryIds[11] = R.id.category_button_12;

    button1 = view.findViewById(R.id.category_button_1);
    button2 = view.findViewById(R.id.category_button_2);
    button3 = view.findViewById(R.id.category_button_3);
    button4 = view.findViewById(R.id.category_button_4);
    button5 = view.findViewById(R.id.category_button_5);
    button6 = view.findViewById(R.id.category_button_6);
    button7 = view.findViewById(R.id.category_button_7);
    button8 = view.findViewById(R.id.category_button_8);
    button9 = view.findViewById(R.id.category_button_9);
    button10 = view.findViewById(R.id.category_button_10);
    button11 = view.findViewById(R.id.category_button_11);
    button12 = view.findViewById(R.id.category_button_12);


    button1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(0);
      }
    });

    button2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(1);
      }
    });

    button3.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(2);
      }
    });

    button4.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(3);
      }
    });

    button5.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(4);
      }
    });

    button6.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(5);
      }
    });

    button7.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(6);
      }
    });

    button8.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(7);
      }
    });

    button9.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(8);
      }
    });

    button10.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(9);
      }
    });

    button11.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(10);
      }
    });

    button12.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(11);
      }
    });

    // Inflate the layout for this fragment
    return view;
  }

  // TODO: fix the stuff missing that SuppressLint is not needed
  private void deselectAllOtherExcept(int currentButton) {
    ConstraintLayout constraintLayout = view.findViewById(R.id.horizontal_categories);
    ConstraintSet constraintSet = new ConstraintSet();
    constraintSet.clone(constraintLayout);

    for (int i = 0; i < categoryIds.length; i++) {
      if (i != currentButton) {
        TextView currentEditing= view.findViewById(categoryIds[i]);

        currentEditing.setTextColor(Color.parseColor("#57102A68"));
        currentEditing.setTextSize(16);
        constraintSet.setVerticalBias(categoryIds[i], 0.8f);
        constraintSet.applyTo(constraintLayout);
      }
      else  {
        TextView currentSelect = view.findViewById(categoryIds[i]);
        currentSelect.setTextColor(Color.parseColor("#102a68"));
        currentSelect.setTextSize(27);
      }
    }
  }

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