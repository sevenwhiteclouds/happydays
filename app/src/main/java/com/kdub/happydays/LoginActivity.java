package com.kdub.happydays;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kdub.happydays.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
  TextView displayStoreNames;
  EditText username;
  EditText password;
  Button singInButton;

  ActivityLoginBinding mActivityLoginBinding = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    mActivityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());

    View view = mActivityLoginBinding.getRoot();
    setContentView(view);

    displayStoreNames = mActivityLoginBinding.logoPosition;
    username = mActivityLoginBinding.usernameField;
    password = mActivityLoginBinding.passwordField;
    singInButton = mActivityLoginBinding.signInButton;

    singInButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // this is where the code for when the user clicks on sign goes
      }
    });

  }
}