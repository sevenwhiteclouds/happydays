package com.kdub.happydays;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

  private View view = null;
  private SharedPreferences preferences = null;
  private HappyDAO mHappyDao = null;
  private TextView button0 = null;
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
  private int[] categoryIds = new int[13];

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_home, container, false);

    preferences = getActivity().getSharedPreferences("session", MODE_PRIVATE);
    mHappyDao = Room.databaseBuilder(getActivity(), AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().happyDAO();

    setTimeOfDayHiMessage();

    // need to save the int value corresponding to each button to easily loop through them later
    categoryIds[0] = R.id.category_button_0;
    categoryIds[1] = R.id.category_button_1;
    categoryIds[2] = R.id.category_button_2;
    categoryIds[3] = R.id.category_button_3;
    categoryIds[4] = R.id.category_button_4;
    categoryIds[5] = R.id.category_button_5;
    categoryIds[6] = R.id.category_button_6;
    categoryIds[7] = R.id.category_button_7;
    categoryIds[8] = R.id.category_button_8;
    categoryIds[9] = R.id.category_button_9;
    categoryIds[10] = R.id.category_button_10;
    categoryIds[11] = R.id.category_button_11;
    categoryIds[12] = R.id.category_button_12;

    // this just makes it easier to reference each button later
    button0 = view.findViewById(R.id.category_button_0);
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

    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
    HomeItemsAdapter adapter = new HomeItemsAdapter(getActivity(), getContext(), (ArrayList<GroceryItem>) mHappyDao.getAllGroceryItems());

    RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

    recyclerView.setLayoutManager(gridLayoutManager);
    recyclerView.setAdapter(adapter);

    CardView searchBar = view.findViewById(R.id.fragment_search_for_item);

    searchBar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (preferences.getBoolean("isAdmin", false)) {
          fragmentTransaction.replace(R.id.frame_layout_admin, new SearchItemHomeFragment());
        }
        else {
          fragmentTransaction.replace(R.id.frame_layout_normal, new SearchItemHomeFragment());
        }
        fragmentTransaction.commit();
      }
    });

    button0.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(0);
        loadRecycleView("all");
      }
    });

    button1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(1);
        loadRecycleView("produce");
      }
    });

    button2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(2);
        loadRecycleView("bread");
      }
    });

    button3.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(3);
        loadRecycleView("meat");
      }
    });

    button4.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(4);
        loadRecycleView("dairy");
      }
    });

    button5.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(5);
        loadRecycleView("frozen goods");
      }
    });

    button6.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(6);
        loadRecycleView("canned goods");
      }
    });

    button7.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(7);
        loadRecycleView("beverages");
      }
    });

    button8.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(8);
        loadRecycleView("baking goods");
      }
    });

    button9.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(9);
        loadRecycleView("cleaners");
      }
    });

    button10.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(10);
        loadRecycleView("paper goods");
      }
    });

    button11.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(11);
        loadRecycleView("personal care");
      }
    });

    button12.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deselectAllOtherExcept(12);
        loadRecycleView("other");
      }
    });

    // Inflate the layout for this fragment
    return view;
  }

  // TODO: fix the stuff missing that SuppressLint is not needed
  private void loadRecycleView(String category) {
    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
    HomeItemsAdapter adapter = null;

    if (category.equals("all")) {
      adapter = new HomeItemsAdapter(getActivity(), getContext(), (ArrayList<GroceryItem>) mHappyDao.getAllGroceryItems());
    }
    else {
      adapter = new HomeItemsAdapter(getActivity(), getContext(), (ArrayList<GroceryItem>) mHappyDao.getGroceryByCategory(category));
    }

    RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);

    recyclerView.setLayoutManager(gridLayoutManager);
    recyclerView.setAdapter(adapter);
  }

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
    TextView subHeadingText = view.findViewById(R.id.home_sub_heading_text_one);

    StringBuilder subHeadingMessage = new StringBuilder();
    subHeadingMessage.append("it's ");
    if (!preferences.getBoolean("isAdmin", false)) {
      subHeadingMessage.append("<b><font color='#ff5522'>cooking</font></b>");
    }
    else {
      subHeadingMessage.append("<b><font color='#ff5522'>working</font></b>");
    }
    subHeadingMessage.append(" time!");

    // not really happy  about using html.fromhtml considering i had to do hack
    // to get it to stop giving me the deprecated message. find a better solution?
    subHeadingText.setText(Html.fromHtml(subHeadingMessage.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));

    int userId = preferences.getInt("userId", 0);
    String firstName = mHappyDao.getUserByUserId(userId).getFirstName();
    firstName = firstName.toLowerCase();
    firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);

    if (currentHour < 12) {
      homeHeadingText.setText("Good morning, " + firstName + " \uD83D\uDC4B");
    }
    else if (currentHour < 18) {
      homeHeadingText.setText("Good afternoon, " + firstName + " \uD83D\uDC4B");
    }
    else {
      homeHeadingText.setText("Good evening, " + firstName + " \uD83D\uDC4B");
    }
  }
}