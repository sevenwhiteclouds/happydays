package com.kdub.happydays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.kdub.happydays.databinding.ActivityLandingPageAdminBinding;
import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;

public class LandingPageAdminActivity extends AppCompatActivity {
  private ActivityLandingPageAdminBinding mActivityLandingPageAdminBinding = null;
  private HappyDAO mHappyDAO;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    getDatabase();

    super.onCreate(savedInstanceState);
    mActivityLandingPageAdminBinding = ActivityLandingPageAdminBinding.inflate(getLayoutInflater());
    setContentView(mActivityLandingPageAdminBinding.getRoot());

    mActivityLandingPageAdminBinding.bottomNavigationViewAdmin.setOnItemSelectedListener(item -> {

      switch (item.getItemId()) {
        case R.id.home_admin: {
          // TODO: add the code for home
          break;
        }
        case R.id.add_item_admin: {
          // TODO: add the code for add item
          break;
        }
        case R.id.orders_admin: {
          // TODO: add the code for orders
          break;
        }
        case R.id.account_settings_admin: {
          // TODO: add the code for account settings
          break;
        }
      }

      return true;
    });

  }

  private void replaceFragment(Fragment fragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.frame_layout_admin, fragment);
    fragmentTransaction.commit();
  }

  private void getDatabase() {
    mHappyDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().happyDAO();
  }
}