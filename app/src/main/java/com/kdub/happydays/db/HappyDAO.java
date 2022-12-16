package com.kdub.happydays.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kdub.happydays.CartItem;
import com.kdub.happydays.GroceryItem;
import com.kdub.happydays.Order;
import com.kdub.happydays.User;

import java.util.List;

@Dao
public interface HappyDAO {
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

  @Query("DELETE FROM " + AppDataBase.USER_TABLE + " WHERE mUserId = :userId ")
  void deleteUserByUserId(int userId);

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

  @Query("SELECT * FROM " + AppDataBase.GROCERY_ITEM_TABLE + " WHERE mCategory = :category ")
  List<GroceryItem> getGroceryByCategory(String category);

  // starting here is all the cart item entry stuff
  @Insert
  void insert(CartItem... cartItems);

  @Update
  void update(CartItem... cartItems);

  @Delete
  void delete(CartItem cartItems);

  @Query("SELECT * FROM " + AppDataBase.CART_ITEMS_TABLE)
  List<CartItem> getAllCartItems();

  @Query("SELECT * FROM " + AppDataBase.CART_ITEMS_TABLE + " WHERE mUserId = :userId ")
  List<CartItem> getCartItemsByUserId(int userId);

  @Query("DELETE FROM " + AppDataBase.CART_ITEMS_TABLE + " WHERE mUserId = :userId ")
  void deleteAllCartEntriesByUserId(int userId);

  // starting here is all the order item entry stuff
  @Insert
  void insert(Order... orders);

  @Update
  void update(Order... orders);

  @Delete
  void delete(Order orders);

  @Query("SELECT * FROM " + AppDataBase.ORDERS_TABLE + " WHERE mUserId = :userId ")
  List<Order> getOrdersByUserId(int userId);

  @Query("SELECT * FROM " + AppDataBase.ORDERS_TABLE +  " WHERE mUserId = :userId " + " ORDER BY mOrderEntryId DESC ")
  List<Order> getOrdersByUserIdDesc(int userId);

  @Query("DELETE FROM " + AppDataBase.ORDERS_TABLE + " WHERE mUserId = :userId ")
  void deleteAllOrdersByUserId(int userId);
}