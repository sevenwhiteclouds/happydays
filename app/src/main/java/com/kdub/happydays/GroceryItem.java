package com.kdub.happydays;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.kdub.happydays.db.AppDataBase;

import java.util.Objects;

@Entity(tableName = AppDataBase.GROCERY_ITEM_TABLE)
public class GroceryItem {
  @PrimaryKey(autoGenerate = true)
  private int mGroceryItemId;

  private String mName;
  private double mQuantity;
  private String mDenomination;
  private double mPrice;
  private int mAmountOfThisItem = 1;
  private String mCategory;
  private int itemImage;
  private int itemAddButton;

  // note: any method that sets private variables do a to lower before setting
  // this is done to have equal() method have predictable outcomes
  public GroceryItem(String category, String name, double quantity, String denomination, double price) {
    mCategory = category.toLowerCase();
    mName = name.toLowerCase();
    mQuantity = quantity;
    mDenomination = denomination.toLowerCase();
    mPrice = price;
  }

  public int getItemImage() {
    return itemImage;
  }

  public void setItemImage(int itemImage) {
    this.itemImage = itemImage;
  }

  public int getItemAddButton() {
    return itemAddButton;
  }

  public void setItemAddButton(int itemAddButton) {
    this.itemAddButton = itemAddButton;
  }

  public int getGroceryItemId() {
    return mGroceryItemId;
  }

  public void setGroceryItemId(int groceryItemId) {
    mGroceryItemId = groceryItemId;
  }

  public String getCategory() {
    return mCategory;
  }

  public void setCategory(String category) {
    mCategory = category.toLowerCase();
  }

  public String getName() {
    return mName;
  }

  public void setName(String name) {
    mName = name.toLowerCase();
  }

  public double getQuantity() {
    return mQuantity;
  }

  public void setQuantity(double quantity) {
    mQuantity = quantity;
  }

  public String getDenomination() {
    return mDenomination;
  }

  public void setDenomination(String denomination) {
    mDenomination = denomination.toLowerCase();
  }

  public double getPrice() {
    return mPrice;
  }

  public void setPrice(double price) {
    mPrice = price;
  }

  public int getAmountOfThisItem() {
    return mAmountOfThisItem;
  }

  public void setAmountOfThisItem(int amountSameItemDatabase) {
    mAmountOfThisItem = amountSameItemDatabase;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GroceryItem that = (GroceryItem) o;
    return Double.compare(that.mQuantity, mQuantity) == 0 && Double.compare(that.mPrice, mPrice) == 0 && Objects.equals(mCategory, that.mCategory) && Objects.equals(mName, that.mName) && Objects.equals(mDenomination, that.mDenomination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mCategory, mName, mQuantity, mDenomination, mPrice);
  }
}