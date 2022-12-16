package com.kdub.happydays;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.kdub.happydays.db.AppDataBase;

@Entity(tableName = AppDataBase.ORDERS_TABLE)
public class Order {
  @PrimaryKey(autoGenerate = true)
  private int mOrderEntryId;

  private int mActiveOrder = 1;
  private int mUserId;
  private String mItems;
  private String mAmountOfItems;
  private Double mTotalOrderPrice;

  public Order(int userId, String items, String amountOfItems, Double totalOrderPrice) {
    mUserId = userId;
    mItems = items;
    mAmountOfItems = amountOfItems;
    mTotalOrderPrice = totalOrderPrice;
  }

  public int getOrderEntryId() {
    return mOrderEntryId;
  }

  public void setOrderEntryId(int orderEntryId) {
    mOrderEntryId = orderEntryId;
  }

  public int getActiveOrder() {
    return mActiveOrder;
  }

  public void setActiveOrder(int activeOrder) {
    mActiveOrder = activeOrder;
  }

  public int getUserId() {
    return mUserId;
  }

  public void setUserId(int userId) {
    mUserId = userId;
  }

  public String getItems() {
    return mItems;
  }

  public void setItems(String items) {
    mItems = items;
  }

  public String getAmountOfItems() {
    return mAmountOfItems;
  }

  public void setAmountOfItems(String amountOfItems) {
    mAmountOfItems = amountOfItems;
  }

  public Double getTotalOrderPrice() {
    return mTotalOrderPrice;
  }

  public void setTotalOrderPrice(Double totalOrderPrice) {
    mTotalOrderPrice = totalOrderPrice;
  }
}