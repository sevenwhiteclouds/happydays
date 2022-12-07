package com.kdub.happydays.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.kdub.happydays.LoginActivity;

@Dao
public interface LoginDAO {
  @Insert
  void insert(LoginActivity... loginActivities);

  @Update
  void update(LoginActivity... loginActivities);

  @Delete
  void delete(LoginActivity... loginActivities);
}
