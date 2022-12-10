package com.kdub.happydays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.os.Bundle;

import com.kdub.happydays.databinding.ActivityHomescreenBinding;
import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.LoginDAO;

public class Homescreen extends AppCompatActivity {
  private ActivityHomescreenBinding mActivityHomescreenBinding = null;

  private LoginDAO mLoginDAO;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    int userID = getIntent().getIntExtra("userID", 0);
    boolean adminAccount = getIntent().getBooleanExtra("adminAccount", false);
    getDatabase();

    super.onCreate(savedInstanceState);
    replaceFragment(new HomeFragment());
    mActivityHomescreenBinding = ActivityHomescreenBinding.inflate(getLayoutInflater());
    setContentView(mActivityHomescreenBinding.getRoot());

    mActivityHomescreenBinding.bottomNavigationView.setOnItemSelectedListener(item -> {

      switch (item.getItemId()) {
        case R.id.home: {
          replaceFragment(new HomeFragment());
          break;
        }
        case R.id.cart: {
          replaceFragment(new CartFragment());
          break;
        }
        case R.id.orders: {
          replaceFragment(new OrdersFragment());
          break;
        }
        case R.id.account_settings: {
          replaceFragment(new AccountSettingsFragment());
          break;
        }
      }

      return true;
    });

  }

  private void replaceFragment(Fragment fragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.frame_layout, fragment);
    fragmentTransaction.commit();
  }

  private void getDatabase() {
    mLoginDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().LoginDAO();
  }
}