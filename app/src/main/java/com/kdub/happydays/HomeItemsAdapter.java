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

  // TODO: find a fix so i don't have to sdmiuppress here
  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull HomeItemsAdapter.MyViewHolder holder, int position) {
    Double quantity = groceryItems.get(position).getQuantity();
    String[] split = quantity.toString().split("[.]");

    if (split[1].length() == 1 && split[1].equals("0")) {
      int wholeNumber = 0;
      wholeNumber = Integer.parseInt(split[0]);
      holder.itemDenominationLeft.setText(wholeNumber + " " + beautify(groceryItems.get(position).getDenomination()));

      System.out.println(split.length);
    }
    else {
      holder.itemDenominationLeft.setText(quantity + " " + beautify(groceryItems.get(position).getDenomination()));
    }

    String itemName = beautify(groceryItems.get(position).getName());
    holder.itemNameLeft.setText(itemName);
    holder.itemPriceLeft.setText("$" + groceryItems.get(position).getPrice());

    holder.itemPictureLeft.setImageResource(R.drawable.beverages);
  }

  private String beautify(String beautifyThis) {
    beautifyThis = beautifyThis.toLowerCase(Locale.ROOT);
    beautifyThis = beautifyThis.substring(0, 1).toUpperCase() + beautifyThis.substring(1);

    return beautifyThis;
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