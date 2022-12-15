package com.kdub.happydays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kdub.happydays.databinding.ActivityCreateAccountBinding;
import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;import com.kdub.happydays.db.HappyDAO;

public class CreateAccount extends AppCompatActivity {
  TextView mFirstName;
  TextView mLastName;
  TextView mUserName;
  TextView mPassword;
  Button mButton;

  private ActivityCreateAccountBinding mActivityCreateAccountBinding = null;

  private HappyDAO mHappyDAO;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_account);

    mActivityCreateAccountBinding = ActivityCreateAccountBinding.inflate(getLayoutInflater());

    View view = mActivityCreateAccountBinding.getRoot();
    setContentView(view);

    // TODO: add a confirm password?
    mFirstName = mActivityCreateAccountBinding.firstNameText;
    mLastName = mActivityCreateAccountBinding.lastNameText;
    mUserName = mActivityCreateAccountBinding.usernameText;
    mPassword = mActivityCreateAccountBinding.passwordText;
    mButton = mActivityCreateAccountBinding.createAccountButton;

    getDatabase();

    mButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String first = mFirstName.getText().toString();
        String last = mLastName.getText().toString();
        String user = mUserName.getText().toString();
        String pass = mPassword.getText().toString();

        if (fieldEmpty(first, last, user, pass)) {
          Toast.makeText(CreateAccount.this, "One or more fields are empty. All fields must be filled in.", Toast.LENGTH_SHORT).show();
        }
        else if (userNameTaken(user)) {
          Toast.makeText(CreateAccount.this, "Username taken. Please choose a different one.", Toast.LENGTH_SHORT).show();
        }
        else {
          int userId = accountCreate(first, last, user, pass);

          Intent intent = new Intent(getApplicationContext(), LandingPageActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);


          saveSession(userId, false);
          startActivity(intent);
        }
      }
    });
  }

  private void saveSession(int userId, boolean isAdmin) {
    SharedPreferences mPreferences = getSharedPreferences("session", MODE_PRIVATE);
    SharedPreferences.Editor mEditor = mPreferences.edit();
    mEditor.putInt("userId", userId);
    mEditor.putBoolean("isAdmin", isAdmin);

    mEditor.commit();
  }

  private boolean fieldEmpty(String first, String last, String user, String pass) {
    return first.length() == 0 || last.length() == 0 || user.length() == 0 || pass.length() == 0;
  }

  private int accountCreate(String firstName, String lastName, String userName, String password) {
    User user = new User(0, firstName, lastName, userName, password);

    mHappyDAO.insert(user);

    return mHappyDAO.getUserByUsername(userName).getUserId();
  }

  private boolean userNameTaken(String userName) {
    for (int i = 0; i < mHappyDAO.getAllUsers().size(); i++) {
      if (mHappyDAO.getAllUsers().get(i).getUserName().equals(userName)) {
        return true;
      }
    }

    return false;
  }

  private void getDatabase() {
    mHappyDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().happyDAO();
  }
}