package com.kdub.happydays;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kdub.happydays.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
  TextView displayStoreNames;
  Button singInButton;
  Button register;

  ActivityMainBinding mActivityMainBinding = null;


  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());

    View view = mActivityMainBinding.getRoot();
    setContentView(view);

    displayStoreNames = mActivityMainBinding.logoPosition;
    singInButton = mActivityMainBinding.signInButton;
    register = mActivityMainBinding.registerButton;

    singInButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // this is where the code for when the user clicks on sign in goes
      }
    });

    register.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // this where the code when the user clicks on register goes
      }
    });
  }
}