package com.kdub.happydays.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kdub.happydays.GroceryItem;
import com.kdub.happydays.User;

import java.util.List;

@Dao
public interface LoginDAO {
  /*
   * Author: Keldin Maldonado
   * Date: 2022-12-06
   * Abstract: Dao file to work with the Android app database
   */

  // starting here is all the user account stuff
  @Insert
  void insert(User... users);

  @Update
  void update(User... users);

  @Delete
  void delete(User users);

  @Query("SELECT * FROM " + AppDataBase.USER_TABLE)
  List<User> getAllUsers();

  @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserName = :username ")
  User getUserByUsername(String username);

  @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserId = :userId ")
  User getUserByUserId(int userId);

  @Query("SELECT EXISTS (SELECT * FROM  USER_TABLE WHERE mUserName = :username )")
  boolean userExist(String username);

  // starting here is all the grocery item stuff
  @Insert
  void insert(GroceryItem... groceryItems);

  @Update
  void update(GroceryItem... groceryItems);

  @Delete
  void delete(GroceryItem groceryItems);

  @Query("SELECT * FROM " + AppDataBase.GROCERY_ITEM_TABLE)
  List<GroceryItem> getAllGroceryItems();

  @Query("SELECT * FROM " + AppDataBase.GROCERY_ITEM_TABLE + " WHERE mGroceryItemId = :groceryItemId ")
  GroceryItem getGroceryItemById(int groceryItemId);
}
