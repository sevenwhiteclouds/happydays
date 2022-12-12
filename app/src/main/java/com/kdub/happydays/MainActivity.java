package com.kdub.happydays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kdub.happydays.databinding.ActivityMainBinding;
import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.LoginDAO;

import java.util.List;

// TODO: make the back buttons inside account settings nicer. also, a confirm logout button
public class MainActivity extends AppCompatActivity {
  private EditText mUsername;
  private EditText mPassword;
  private Button mButtonLogin;
  private Button mButtonRegister;

  private ActivityMainBinding mActivityMainBinding = null;

  private LoginDAO mLoginDao;

  // TODO: IMPORTANT if you have time, fix the screen rotation across whole app
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());

    View view = mActivityMainBinding.getRoot();
    setContentView(view);

    mUsername = mActivityMainBinding.usernameField;
    mPassword = mActivityMainBinding.passwordField;
    mButtonLogin = mActivityMainBinding.signInButton;
    mButtonRegister = mActivityMainBinding.createAccountButton;

    getDatabase();

    if (sessionExists()) {
      Intent intent = new Intent(getApplicationContext(), Homescreen.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(intent);
    }

    createDefaultUsers();
    createDefaultGroceryItems();

    mButtonLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        boolean adminAccount = false;
        int userId = 0;

        int userNameLengthText = mUsername.getText().length();
        int passWordLengthText = mPassword.getText().length();


        if (userNameLengthText != 0 || passWordLengthText != 0) {
          userId = logIn(mUsername.getText().toString(), mPassword.getText().toString());
        }

        if (userId < 0) {
          Toast.makeText(MainActivity.this, "The User ID or Password is incorrect. Please try again.", Toast.LENGTH_SHORT).show();
        }
        else if (userId > 0) {
          adminAccount = adminUserCheck(userId);

          // preparing to take off!
          Intent intent = new Intent(getApplicationContext(), Homescreen.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

          // saving to shared preferences before starting activity
          saveSession(userId, adminAccount);

          // take off! from line 72 go go go
          startActivity(intent);
        }
      }
    });

    mButtonRegister.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateAccount.class);
        startActivity(intent);
      }
    });
  }

  private void addItemToStore(GroceryItem testThisItem) {
    List<GroceryItem> groceryItems = mLoginDao.getAllGroceryItems();

    boolean itemExists = false;
    int atWhatIndexSameItemExists = 0;

    // this is used to figure out where in the database the same item exists
    // also, this sets the flag that the item exists
    for (int i = 0; i < groceryItems.size(); i++) {
      if (groceryItems.get(i).equals(testThisItem)) {
        itemExists = true;
        atWhatIndexSameItemExists = i;
        break;
      }
    }

    if (itemExists){
      int currentAmountSameItemDatabase = groceryItems.get(atWhatIndexSameItemExists).getAmountOfThisItem();

      groceryItems.get(atWhatIndexSameItemExists).setAmountOfThisItem(currentAmountSameItemDatabase+1);
      mLoginDao.update(groceryItems.get(atWhatIndexSameItemExists));
    }
    else {
      mLoginDao.insert(testThisItem);
    }
  }

  private void createDefaultGroceryItems() {
    if (mLoginDao.getAllGroceryItems().size() == 0) {
      GroceryItem item1 = new GroceryItem("produce", "aPpLe", 1, "PIECE", 1.50);
      GroceryItem item2 = new GroceryItem("produce", "cucumber", 1, "PIECE", 2.00);
      GroceryItem item3 = new GroceryItem("produce", "bananas", 3, "PIECE", 3.00);
      GroceryItem item4 = new GroceryItem("produce", "apple", 1, "PIECE", 1.50);
      GroceryItem item5 = new GroceryItem("produce", "bananas", 3, "PIECE", 3.00);
      GroceryItem item6 = new GroceryItem("produce", "bananas", 3, "PIECE", 3.00);
      GroceryItem item7 = new GroceryItem("produce", "bananas", 3, "PIECE", 3.00);
      GroceryItem item8 = new GroceryItem("produce", "cucumber", 1, "PIECE", 2.00);
      GroceryItem item9 = new GroceryItem("produce", "lettuce", 1, "PIECE", 2.00);
      GroceryItem item10 = new GroceryItem("beverages", "Pina colada", 2, "piece", 8.75);
      GroceryItem item11 = new GroceryItem("beverages", "Mai tai", 1, "piece", 4.25);
      GroceryItem item12 = new GroceryItem("beverages", "Sprite", 1.5, "lt", 3.75);
      GroceryItem item13 = new GroceryItem("beverages", "Orange juice", 1, "piece", 3.99);
      GroceryItem item14 = new GroceryItem("beverages", "Nescafe Coffee", 1, "piece", 4.65);
      GroceryItem item15 = new GroceryItem("beverages", "Don Julio 1942", 1, "piece", 249.99);
      GroceryItem item16 = new GroceryItem("Bread", "Ezekiel Bread", 1, "piece", 6.49);
      GroceryItem item17 = new GroceryItem("produce", "Grapefruit", 10, "piece", 5.15);
      GroceryItem item18 = new GroceryItem("produce", "Strawberries", 15, "piece", 7.65);
      GroceryItem item19 = new GroceryItem("dry/baking goods", "rice", 2.5, "lb", 3.20);
      GroceryItem item20 = new GroceryItem("dry/baking goods", "rice", 2.5, "lb", 3.20);

      addItemToStore(item1);
      addItemToStore(item2);
      addItemToStore(item3);
      addItemToStore(item4);
      addItemToStore(item5);
      addItemToStore(item6);
      addItemToStore(item7);
      addItemToStore(item8);
      addItemToStore(item9);
      addItemToStore(item10);
      addItemToStore(item11);
      addItemToStore(item12);
      addItemToStore(item13);
      addItemToStore(item14);
      addItemToStore(item15);
      addItemToStore(item16);
      addItemToStore(item17);
      addItemToStore(item18);
      addItemToStore(item19);
      addItemToStore(item20);
    }
  }

  private void saveSession(int userId, boolean isAdmin) {
    SharedPreferences mPreferences = getSharedPreferences("session", MODE_PRIVATE);
    SharedPreferences.Editor mEditor = mPreferences.edit();
    mEditor.putInt("userId", userId);
    mEditor.putBoolean("isAdmin", isAdmin);

    mEditor.commit();
  }

  private boolean sessionExists() {
    SharedPreferences mPreferences = getSharedPreferences("session", MODE_PRIVATE);

    return mPreferences.contains("userId");
  }

  private void getDatabase() {
    mLoginDao = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().LoginDAO();
  }

  private void createDefaultUsers() {
    // persistent users even if they are deleted sometime in the future
    // THESE. USERS. ARE. NEEDED. IN. THE. APP.
    if (!mLoginDao.userExist("testuser1")) {
      User defaultNormalUser = new User(0, "user", "default", "testuser1", "testuser1");
      mLoginDao.insert(defaultNormalUser);
    }

    if (!mLoginDao.userExist("admin2")) {
      User defaultAdminUser = new User(1, "admin", "default", "admin2", "admin2");
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