package com.kdub.happydays.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kdub.happydays.CartItem;
import com.kdub.happydays.GroceryItem;
import com.kdub.happydays.User;

@Database(entities = {User.class, GroceryItem.class, CartItem.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
  public static final String DATABASE_NAME = "HAPPYDAYS_DATABASE";
  public static final String USER_TABLE = "USER_TABLE";
  public static final String GROCERY_ITEM_TABLE = "GROCERY_ITEM_TABLE";
  public static final String CART_ITEMS_TABLE = "CART_ITEMS_TABLE";

  private static volatile AppDataBase instance;
  private static final Object LOCK = new Object();

  public abstract HappyDAO happyDAO();

  public static AppDataBase getInstance(Context context) {
    if (instance == null) {
      synchronized (LOCK) {
        if (instance == null) {
          instance = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, DATABASE_NAME).build();
        }
      }
    }

    return instance;
  }
}