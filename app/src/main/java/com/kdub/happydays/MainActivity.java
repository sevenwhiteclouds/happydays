package com.kdub.happydays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kdub.happydays.databinding.ActivityMainBinding;
import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;import com.kdub.happydays.db.HappyDAO;

import java.util.ArrayList;
import java.util.List;

// TODO: make the back buttons inside account settings nicer. also, a confirm logout button
public class MainActivity extends AppCompatActivity {
  private EditText mUsername;
  private EditText mPassword;
  private Button mButtonLogin;
  private Button mButtonRegister;

  private ActivityMainBinding mActivityMainBinding = null;

  private HappyDAO mHappyDao;

  private SharedPreferences mPreferences;

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

    // setting the shared preferences
    mPreferences = getSharedPreferences("session", MODE_PRIVATE);

    getDatabase();

    if (sessionExists()) {
      if (mPreferences.getBoolean("isAdmin", false)) {
        Intent intent = new Intent(getApplicationContext(), LandingPageAdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
      }
      else {
        Intent intent = new Intent(getApplicationContext(), LandingPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
      }
    }

    createDefaultUsers();
    createDefaultGroceryItems();
    createDefaultTaxAmount();

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

          Intent intent;
          if (adminAccount) {
            intent = new Intent(getApplicationContext(), LandingPageAdminActivity.class);
          }
          else {
            intent = new Intent(getApplicationContext(), LandingPageActivity.class);
          }

          // preparing to take off!
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

          // saving to shared preferences before starting activity
          saveSession(userId, adminAccount);

          // take off! from line 109 go go go
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
    List<GroceryItem> groceryItems = mHappyDao.getAllGroceryItems();

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
      mHappyDao.update(groceryItems.get(atWhatIndexSameItemExists));
    }
    else {
      mHappyDao.insert(testThisItem);
    }
  }

  // TODO: this desperately needs to be cleaned up some way so i don't have this huge method lol
  private void createDefaultGroceryItems() {
    if (mHappyDao.getAllGroceryItems().size() == 0) {
      List<GroceryItem> allDefaultItems = new ArrayList<>();

      allDefaultItems.add(new GroceryItem("produce", "aPpLe", 1, "PIECE", 1.50, R.drawable.produce));
      allDefaultItems.add(new GroceryItem("produce", "cucumber", 1, "PIECE", 2.00, R.drawable.produce));
      allDefaultItems.add(new GroceryItem("produce", "bananas", 3, "PIECE", 3.00, R.drawable.produce));
      allDefaultItems.add(new GroceryItem("produce", "apple", 1, "PIECE", 1.50, R.drawable.produce));
      allDefaultItems.add(new GroceryItem("produce", "bananas", 3, "PIECE", 3.00, R.drawable.produce));
      allDefaultItems.add(new GroceryItem("produce", "bananas", 3, "PIECE", 3.00, R.drawable.produce));
      allDefaultItems.add(new GroceryItem("produce", "bananas", 3, "PIECE", 3.00, R.drawable.produce));
      allDefaultItems.add(new GroceryItem("produce", "cucumber", 1, "PIECE", 2.00, R.drawable.produce));
      allDefaultItems.add(new GroceryItem("produce", "lettuce", 1, "PIECE", 2.00, R.drawable.produce));
      allDefaultItems.add(new GroceryItem("beverages", "Pina colada", 2, "piece", 8.75, R.drawable.beverages));
      allDefaultItems.add(new GroceryItem("beverages", "Mai tai", 1, "piece", 4.25, R.drawable.beverages));
      allDefaultItems.add(new GroceryItem("beverages", "Sprite", 1.5, "lt", 3.75, R.drawable.beverages));
      allDefaultItems.add(new GroceryItem("beverages", "Orange juice", 1, "piece", 3.99, R.drawable.beverages));
      allDefaultItems.add(new GroceryItem("beverages", "Nescafe Coffee", 1, "piece", 4.65, R.drawable.beverages));
      allDefaultItems.add(new GroceryItem("beverages", "Don Julio 1942", 1, "piece", 249.99, R.drawable.beverages));
      allDefaultItems.add(new GroceryItem("Bread", "Ezekiel Bread", 1, "piece", 6.49, R.drawable.bread));
      allDefaultItems.add(new GroceryItem("produce", "Grapefruit", 10, "piece", 5.15, R.drawable.produce));
      allDefaultItems.add(new GroceryItem("produce", "Strawberries", 15, "piece", 7.65, R.drawable.produce));
      allDefaultItems.add(new GroceryItem("baking goods", "rice", 2.5, "lb", 3.20, R.drawable.bake));
      allDefaultItems.add(new GroceryItem("baking goods", "rice", 2.5, "lb", 3.20, R.drawable.bake));
      allDefaultItems.add(new GroceryItem("meat", "Bacon", 10, "piece", 4.20, R.drawable.meat));
      allDefaultItems.add(new GroceryItem("dairy", "milk", 1, "gal", 3.25, R.drawable.dairy));
      allDefaultItems.add(new GroceryItem("frozen goods", "ice cream", 1, "piece", 4.25, R.drawable.frozen));
      allDefaultItems.add(new GroceryItem("canned goods", "Canned beans", 1, "piece", 5.65, R.drawable.canned_food));
      allDefaultItems.add(new GroceryItem("baking goods", "sugar", 2.2, "lb", 4.99, R.drawable.bake));
      allDefaultItems.add(new GroceryItem("cleaners", "Clorox bleach", 1, "gal", 5.99, R.drawable.cleaners));
      allDefaultItems.add(new GroceryItem("paper goods", "Toilet paper", 1, "piece", 3.95, R.drawable.paper_goods));
      allDefaultItems.add(new GroceryItem("personal care", "shaving cream", 1, "piece", 5.95, R.drawable.personal_hygiene));
      allDefaultItems.add(new GroceryItem("other", "Toy car", 1, "piece", 10.95, R.drawable.other));
      allDefaultItems.add(new GroceryItem("baking goods", "flower", 2.05, "lb", 2.99, R.drawable.bake));
      allDefaultItems.add(new GroceryItem("meat", "Chicken", 2.5, "lb", 5.00, R.drawable.meat));

      for (int i = 0; i < allDefaultItems.size(); i++) {
        addItemToStore(allDefaultItems.get(i));
      }
    }
  }

  private void saveSession(int userId, boolean isAdmin) {
    SharedPreferences.Editor mEditor = mPreferences.edit();
    mEditor.putInt("userId", userId);
    mEditor.putBoolean("isAdmin", isAdmin);

    mEditor.apply();
  }

  private boolean sessionExists() {
    return mPreferences.contains("userId");
  }

  private void getDatabase() {
    mHappyDao = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().happyDAO();
  }

  private void createDefaultTaxAmount() {
    SharedPreferences taxEntry = getSharedPreferences("defaultTaxEntry", MODE_PRIVATE);
    SharedPreferences.Editor mEditor = taxEntry.edit();
    mEditor.putFloat("tax", 0.08f);
    mEditor.apply();
  }

  private void createDefaultUsers() {
    // persistent users even if they are deleted sometime in the future
    // THESE. USERS. ARE. NEEDED. IN. THE. APP.
    if (!mHappyDao.userExist("testuser1")) {
      User defaultNormalUser = new User(0, "Joe", "Blow", "testuser1", "testuser1");
      mHappyDao.insert(defaultNormalUser);
    }

    if (!mHappyDao.userExist("admin2")) {
      User defaultAdminUser = new User(1, "Drew", "Clinkenbeard", "admin2", "admin2");
      mHappyDao.insert(defaultAdminUser);
    }
  }

  private int logIn(String username, String password) {
    if (userExists(username)) {
      if (userMatchPassword(username, password)) {
        return mHappyDao.getUserByUsername(username).getUserId();
      }
    }

    return -9999;
  }

  private boolean userMatchPassword(String username, String password) {
    if (mHappyDao.getUserByUsername(username).getPassword().equals(password)) {
      return true;
    }

    return false;
  }

  private boolean userExists(String username) {
    for (int i = 0; i < mHappyDao.getAllUsers().size(); i++) {
      if (mHappyDao.getAllUsers().get(i).getUserName().equals(username)) {
        return true;
      }
    }

    return false;
  }

  private boolean adminUserCheck(int userId) {
    if (mHappyDao.getUserByUserId(userId).getIsAdmin() == 1) {
      return true;
    }

    return false;
  }
}