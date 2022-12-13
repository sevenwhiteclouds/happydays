package com.kdub.happydays;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class HomeItemsAdapter extends RecyclerView.Adapter<HomeItemsAdapter.MyViewHolder> {
  private Context context = null;
  private List<GroceryItem> groceryItems = null;

  public HomeItemsAdapter(Context context, List<GroceryItem> groceryItems) {
    this.context = context;
    this.groceryItems = groceryItems;
  }

  @NonNull
  @Override
  public HomeItemsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.home_item, parent, false);

    return new HomeItemsAdapter.MyViewHolder(view);
  }

  // TODO: find a fix so i don't have to supresslint here
  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull HomeItemsAdapter.MyViewHolder holder, int position) {
    holder.itemPictureLeft.setImageResource(groceryItems.get(position).getItemImage());
    holder.itemNameLeft.setText(beautifyItemName(groceryItems.get(position).getName()));
    holder.itemDenominationLeft.setText(beautifyItemQuantityAndDenomination(groceryItems.get(position).getQuantity(), groceryItems.get(position).getDenomination()));
    holder.itemPriceLeft.setText(beautifyItemPrice(groceryItems.get(position).getPrice()));
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
    return groceryItems.size();
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView itemPictureLeft;
    TextView itemNameLeft;
    TextView itemDenominationLeft;
    TextView itemPriceLeft;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);

      itemPictureLeft = itemView.findViewById(R.id.recycle_item_picture_left);
      itemNameLeft = itemView.findViewById(R.id.recycle_item_name_left);
      itemDenominationLeft = itemView.findViewById(R.id.recycle_item_denomination_left);
      itemDenominationLeft = itemView.findViewById(R.id.recycle_item_denomination_left);
      itemPriceLeft = itemView.findViewById(R.id.recycle_item_price_left);
    }
  }
}