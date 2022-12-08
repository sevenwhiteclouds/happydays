package com.kdub.happydays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kdub.happydays.databinding.ActivityHomescreenBinding;
import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HomeDataDAO;

public class Homescreen extends AppCompatActivity {
  private ActivityHomescreenBinding mActivityHomescreenBinding = null;

  private HomeDataDAO mHomeDataDAO;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    int userID = getIntent().getIntExtra("userID", 0);
    boolean adminAccount = getIntent().getBooleanExtra("adminAccount", false);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_homescreen);

    mActivityHomescreenBinding = ActivityHomescreenBinding.inflate(getLayoutInflater());

    View view = mActivityHomescreenBinding.getRoot();

    setContentView(view);

    if (adminAccount) {
      findViewById(R.id.admin_text).setVisibility(View.VISIBLE);
    }


    getDatabase();


    // TODO: more here


  }

  private void getDatabase() {
    mHomeDataDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().mHomeDataDAO();
  }
}