package com.kdub.happydays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kdub.happydays.databinding.ActivityLoginBinding;
import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.LoginDAO;

import java.util.List;

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

    List<User> add = mLoginDao.getAllUsers();

    if (add.size() == 0) {
      User testuser1 = new User(1, 0, "testuser1", "testuser1");
      User admin = new User(2, 1, "admin", "admin2");

      mLoginDao.insert(testuser1);
      mLoginDao.insert(admin);

    }

    mButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (mUsername.getText().toString().equals(mLoginDao.getUserByUsername(2).getUserName()) && mPassword.getText().toString().equals(mLoginDao.getUserByUsername(2).getPassword())) {
          Toast.makeText(LoginActivity.this, "you are an admin", Toast.LENGTH_SHORT).show();
        } 
        else {
          Toast.makeText(LoginActivity.this, "you are not", Toast.LENGTH_SHORT).show();
        }
      }
    });

  }

  private void getDatabase() {
    mLoginDao = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().LoginDAO();
  }
}