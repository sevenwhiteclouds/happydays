package com.kdub.happydays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kdub.happydays.databinding.ActivityLoginBinding;
import com.kdub.happydays.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
  TextView mDisplayStoreNames;
  Button mSingInButton;
  Button mRegister;

  ActivityMainBinding mActivityMainBinding = null;


  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

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