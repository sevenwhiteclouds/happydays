package com.kdub.happydays;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.kdub.happydays.db.AppDataBase;

@Entity(tableName = AppDataBase.USER_TABLE)
public class User {
  /*
   * Author: Keldin Maldonado
   * Date: 2022-12-06
   * Abstract: User object file that works with the rest of
   * the database in the android application.
   */
  @PrimaryKey(autoGenerate = true)
  private int mUserId;

  private int mIsAdmin;
  private String mUserName;
  private String mPassword;

  public User(int userId, int isAdmin, String userName, String password) {
    mUserId = userId;
    mIsAdmin = isAdmin;
    mUserName = userName;
    mPassword = password;
  }

  public int getIsAdmin() {
    return mIsAdmin;
  }

  public void setIsAdmin(int isAdmin) {
    mIsAdmin = isAdmin;
  }

  public int getUserId() {
    return mUserId;
  }

  public void setUserId(int userId) {
    mUserId = userId;
  }

  public String getUserName() {
    return mUserName;
  }

  public void setUserName(String userName) {
    mUserName = userName;
  }

  public String getPassword() {
    return mPassword;
  }

  public void setPassword(String password) {
    mPassword = password;
  }
}
