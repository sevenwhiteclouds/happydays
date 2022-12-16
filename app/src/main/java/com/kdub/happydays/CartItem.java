package com.kdub.happydays;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.kdub.happydays.db.AppDataBase;

import java.util.Objects;

@Entity(tableName = AppDataBase.CART_ITEMS_TABLE)
public class CartItem {
  @PrimaryKey(autoGenerate = true)
  private int mCartEntryId;

  private int mUserId;
  private int mGroceryItemId;
  private int mHowManyGroceryItemInCart = 1;

  public CartItem(int userId, int groceryItemId) {
    mUserId = userId;
    mGroceryItemId = groceryItemId;
  }

  public int getCartEntryId() {
    return mCartEntryId;
  }

  public void setCartEntryId(int cartEntryId) {
    mCartEntryId = cartEntryId;
  }

  public int getUserId() {
    return mUserId;
  }

  public void setUserId(int userId) {
    mUserId = userId;
  }

  public int getGroceryItemId() {
    return mGroceryItemId;
  }

  public void setGroceryItemId(int groceryItemId) {
    mGroceryItemId = groceryItemId;
  }

  public int getHowManyGroceryItemInCart() {
    return mHowManyGroceryItemInCart;
  }

  public void setHowManyGroceryItemInCart(int howManyGroceryItemInCart) {
    mHowManyGroceryItemInCart = howManyGroceryItemInCart;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CartItem cartItem = (CartItem) o;
    return mUserId == cartItem.mUserId && mGroceryItemId == cartItem.mGroceryItemId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(mUserId, mGroceryItemId);
  }

  // TODO: still need to do the the to string and the equals
}
