package com.kdub.happydays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.os.Bundle;

import com.kdub.happydays.databinding.ActivityLandingPageBinding;
import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;

public class LandingPageActivity extends AppCompatActivity {
  private ActivityLandingPageBinding mActivityLandingPageBinding = null;
  private HappyDAO mHappyDAO;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    getDatabase();

    super.onCreate(savedInstanceState);
    replaceFragment(new HomeFragment());
    mActivityLandingPageBinding = ActivityLandingPageBinding.inflate(getLayoutInflater());
    setContentView(mActivityLandingPageBinding.getRoot());

    mActivityLandingPageBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
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
    fragmentTransaction.replace(R.id.frame_layout_normal, fragment);
    fragmentTransaction.commit();
  }

  private void getDatabase() {
    mHappyDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().happyDAO();
  }
}