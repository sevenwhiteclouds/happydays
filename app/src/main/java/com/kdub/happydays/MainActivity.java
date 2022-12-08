package com.kdub.happydays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kdub.happydays.databinding.ActivityLoginBinding;
import com.kdub.happydays.databinding.ActivityMainBinding;
import com.kdub.happydays.db.AppDataBase;

public class MainActivity extends AppCompatActivity {
  /*
   * Author: Keldin Maldonado
   * Date : 2022-12-06
   * Abstract: This is the file that run the screen users
   * see when they first open the app and they are not signed in
   */
  TextView mDisplayStoreNames;
  Button mSingInButton;
  Button mRegister;


  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ActivityMainBinding mActivityMainBinding =
      mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());

    View view = mActivityMainBinding.getRoot();
    setContentView(view);

    mDisplayStoreNames = mActivityMainBinding.logoPosition;
    mSingInButton = mActivityMainBinding.signInButton;
    mRegister = mActivityMainBinding.registerButton;

    mSingInButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // this is where the code for when the user clicks on sign in goes
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
      }
    });

    mRegister.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // this where the code when the user clicks on register goes
      }
    });
  }
}