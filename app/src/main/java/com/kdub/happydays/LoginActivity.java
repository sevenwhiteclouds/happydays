package com.kdub.happydays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kdub.happydays.databinding.ActivityLoginBinding;
import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.LoginDAO;

public class LoginActivity extends AppCompatActivity {
  private EditText mUsername;
  private EditText mPassword;
  private Button mButton;

  private ActivityLoginBinding mActivityLoginBinding = null;

  private LoginDAO mLoginDao;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    mActivityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());

    View view = mActivityLoginBinding.getRoot();
    setContentView(view);

    mUsername = mActivityLoginBinding.usernameField;
    mPassword = mActivityLoginBinding.passwordField;
    mButton = mActivityLoginBinding.signInButton;

    getDatabase();
    createDefaultUsers();


    mButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        boolean adminAccount = false;
        int userId = logIn(mUsername.getText().toString(), mPassword.getText().toString());

        if (fieldEmpty()) {
          Toast.makeText(LoginActivity.this, "One or more fields are empty. All fields must be filled in.", Toast.LENGTH_SHORT).show();
        }
        else if (userId < 0) {
          Toast.makeText(LoginActivity.this, "The User ID or Password is incorrect. Please try again.", Toast.LENGTH_SHORT).show();
        }
        else {
          adminAccount = adminUserCheck(userId);

          Intent intent = new Intent(getApplicationContext(), Homescreen.class);
          intent.putExtra("userId", userId);
          intent.putExtra("adminAccount", adminAccount);
          startActivity(intent);
        }
      }
    });
}

// TODO: clean up the whole long .getText.length stuff
  private boolean fieldEmpty() {
    return mUsername.getText().toString().length() == 0 || mPassword.getText().toString().length() == 0;
  }

  private void getDatabase() {
    mLoginDao = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().LoginDAO();
  }

  private void createDefaultUsers() {
    if (mLoginDao.getAllUsers().size() ==  0) {
      User defaultNormalUser = new User(0, "user", "default", "testuser1", "testuser1");
      User defaultAdminUser = new User(1, "admin", "default", "admin2", "admin2");
      mLoginDao.insert(defaultNormalUser);
      mLoginDao.insert(defaultAdminUser);
    }
  }

  private int logIn(String username, String password) {
    if (userExists(username)) {
      if (userMatchPassword(username, password)) {
        return mLoginDao.getUserByUsername(username).getUserId();
      }
    }

    return -9999;
  }

  private boolean userMatchPassword(String username, String password) {
    if (mLoginDao.getUserByUsername(username).getPassword().equals(password)) {
      return true;
    }

    return false;
  }

  private boolean userExists(String username) {
    for (int i = 0; i < mLoginDao.getAllUsers().size(); i++) {
      if (mLoginDao.getAllUsers().get(i).getUserName().equals(username)) {
        return true;
      }
    }

    return false;
  }

  private boolean adminUserCheck(int userId) {
    if (mLoginDao.getUserByUserId(userId).getIsAdmin() == 1) {
      return true;
    }

    return false;
  }
}