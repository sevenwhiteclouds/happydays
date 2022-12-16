package com.kdub.happydays;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;

import java.util.List;
import java.util.Locale;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder> {
  private Context context;
  private List<CartItem> userCartItemEntries;
  private HappyDAO mHappyDao = null;

  public CartItemAdapter(Context context, List<CartItem> userCartItemEntries) {
    this.context = context;
    this.userCartItemEntries = userCartItemEntries;
    mHappyDao = Room.databaseBuilder(context, AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().happyDAO();
  }

  @NonNull
  @Override
  public CartItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);

    View view = inflater.inflate(R.layout.cart_item, parent, false);

    return new CartItemAdapter.MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull CartItemAdapter.MyViewHolder holder, int position) {
    // TODO: ability to remove an item, right now we are able to go < -1
    int itemId = userCartItemEntries.get(position).getGroceryItemId();

    GroceryItem groceryItem = mHappyDao.getGroceryItemById(itemId);

    holder.cartItemImage.setImageResource(groceryItem.getItemImage());
    holder.cartItemName.setText(beautifyItemName(groceryItem.getName()));
    holder.cartItemDenomination.setText(beautifyItemQuantityAndDenomination(groceryItem.getQuantity(), groceryItem.getDenomination()));
    holder.cartItemPrice.setText(beautifyItemPrice(groceryItem.getPrice()));
    holder.cartItemAmountOfItem.setText(howManyGroceryItemInCart(position));
    
    holder.addButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        userCartItemEntries.get(position).setHowManyGroceryItemInCart(userCartItemEntries.get(position).getHowManyGroceryItemInCart() + 1);
        mHappyDao.update(userCartItemEntries.get(position));
        holder.cartItemAmountOfItem.setText(howManyGroceryItemInCart(position));
      }
    });

    holder.removeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        userCartItemEntries.get(position).setHowManyGroceryItemInCart(userCartItemEntries.get(position).getHowManyGroceryItemInCart() - 1);
        mHappyDao.update(userCartItemEntries.get(position));
        holder.cartItemAmountOfItem.setText(howManyGroceryItemInCart(position));
      }
    });
  }

  private String howManyGroceryItemInCart(int position) {
    return String.valueOf(userCartItemEntries.get(position).getHowManyGroceryItemInCart());
  }

  private String beautifyItemPrice(Double beautifyThisPrice) {
    String[] splitPrice = beautifyThisPrice.toString().split("[.]");
    StringBuilder beautifiedItemPrice = new StringBuilder();

    beautifiedItemPrice.append("$");

    if (splitPrice[1].length() == 1) {
      beautifiedItemPrice.append(splitPrice[0]).append(".").append(splitPrice[1]).append("0 ");
    }
    else {
      beautifiedItemPrice.append(splitPrice[0]).append(".").append(splitPrice[1]).append(" ");
    }

    beautifiedItemPrice = new StringBuilder(beautifiedItemPrice.substring(0, beautifiedItemPrice.length() - 1));

    return beautifiedItemPrice.toString();
  }

  private String beautifyItemQuantityAndDenomination(Double beautifyThisItemQuantity, String beautifyThisItemDenomination) {
    String[] splitQuantity = beautifyThisItemQuantity.toString().split("[.]");
    String[] splitDenomination = beautifyThisItemDenomination.toString().split("\\s");

    StringBuilder beautifiedItemQuantityAndDenomination = new StringBuilder();

    if (splitQuantity[1].length() == 1) {
      if (splitQuantity[1].equals("0")) {
        beautifiedItemQuantityAndDenomination.append(splitQuantity[0]).append(" ");
      }
      else {
        beautifiedItemQuantityAndDenomination.append(splitQuantity[0]).append(".").append(splitQuantity[1]).append("0 ");
      }
    }
    else {
      beautifiedItemQuantityAndDenomination.append(splitQuantity[0]).append(".").append(splitQuantity[1]).append(" ");
    }

    for (int i = 0; i < splitDenomination.length; i++) {
      splitDenomination[i] = splitDenomination[i].toLowerCase(Locale.ROOT);
      splitDenomination[i] = splitDenomination[i].substring(0, 1).toUpperCase() + splitDenomination[i].substring(1);
      beautifiedItemQuantityAndDenomination.append(splitDenomination[i]).append(" ");
    }

    // cleaning up the last character that is always a space that was added by the previous loop
    beautifiedItemQuantityAndDenomination = new StringBuilder(beautifiedItemQuantityAndDenomination.substring(0, beautifiedItemQuantityAndDenomination.length() - 1));

    return beautifiedItemQuantityAndDenomination.toString();
  }

  private  String beautifyItemName(String beautifyThisName) {
    String[] split = beautifyThisName.split("\\s");
    StringBuilder beautifiedName = new StringBuilder();

    for (int i = 0; i < split.length; i++) {
      split[i] = split[i].toLowerCase(Locale.ROOT);
      split[i] = split[i].substring(0, 1).toUpperCase() + split[i].substring(1);
      beautifiedName.append(split[i]).append(" ");
    }

    // cleaning up the last character that is always a space that was added by the previous loop
    beautifiedName = new StringBuilder(beautifiedName.substring(0, beautifiedName.length() - 1));

    return beautifiedName.toString();
  }

  @Override
  public int getItemCount() {
    return userCartItemEntries.size();
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView cartItemImage;
    TextView cartItemName;
    TextView cartItemDenomination;
    TextView cartItemPrice;
    TextView cartItemAmountOfItem;
    ImageView addButton;
    ImageView removeButton;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);

      cartItemImage = itemView.findViewById(R.id.cart_recycle_view_relative_layout_item_image);
      cartItemName = itemView.findViewById(R.id.cart_recycle_view_relative_layout_item_name);
      cartItemDenomination = itemView.findViewById(R.id.cart_recycle_view_relative_layout_item_denomination);
      cartItemPrice = itemView.findViewById(R.id.cart_recycle_view_relative_layout_item_price);
      cartItemAmountOfItem = itemView.findViewById(R.id.cart_recycle_view_amount_in_cart);
      addButton = itemView.findViewById(R.id.cart_recycle_view_relative_layout_item_add_button);
      removeButton = itemView.findViewById(R.id.cart_recycle_view_relative_layout_item_remove_button);
    }
  }
}
