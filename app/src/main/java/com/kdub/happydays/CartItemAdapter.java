package com.kdub.happydays;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder> {
  private Context context;
  private List<CartItem> userCartItemEntries;
  private HappyDAO mHappyDao;
  private Double runningSubTotal;
  private Double runningTax;
  public static Double runningFinalTotal;
  private TextView cartItemSubTotalText;
  private TextView cartItemTaxText;
  private TextView cartItemFinalTotalText;
  private View outSideView;

  public CartItemAdapter(View outSideView, TextView cartItemSubTotalText, TextView cartItemTaxText, TextView cartItemFinalTotalText, Context context, List<CartItem> userCartItemEntries) {
    this.outSideView = outSideView;
    this.cartItemSubTotalText = cartItemSubTotalText;
    this.cartItemTaxText = cartItemTaxText;
    this.cartItemFinalTotalText = cartItemFinalTotalText;
    this.context = context;
    this.userCartItemEntries = userCartItemEntries;
    mHappyDao = Room.databaseBuilder(context, AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().happyDAO();

    inits();
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
        inits();
      }
    });

    holder.removeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (userCartItemEntries.get(position).getHowManyGroceryItemInCart() > 1) {
          userCartItemEntries.get(position).setHowManyGroceryItemInCart(userCartItemEntries.get(position).getHowManyGroceryItemInCart() - 1);
          mHappyDao.update(userCartItemEntries.get(position));
          holder.cartItemAmountOfItem.setText(howManyGroceryItemInCart(position));
        }
        else {
          mHappyDao.delete(userCartItemEntries.get(position));
          userCartItemEntries.remove(position);
          notifyItemRemoved(position);
          notifyItemRangeChanged(position, userCartItemEntries.size());
        }

        inits();

        if (userCartItemEntries.size() == 0) {
          emptyCart();
        }
      }
    });
  }

  private void emptyCart() {
    outSideView.findViewById(R.id.confirm_order_card_view).setVisibility(View.GONE);
    outSideView.findViewById(R.id.order_info_card_view).setVisibility(View.GONE);
    outSideView.findViewById(R.id.cart_header_text).setVisibility(View.GONE);
    outSideView.findViewById(R.id.cart_sad_face).setVisibility(View.VISIBLE);
    outSideView.findViewById(R.id.cart_empty_message).setVisibility(View.VISIBLE);
  }

  private void inits() {
    initRunningSubtotal();
    initTaxes();
    initRunningFinalTotal();

    cartItemSubTotalText.setText(beautifySubTotal());
    cartItemTaxText.setText(beautifyTaxes());
    cartItemFinalTotalText.setText(beautifyFinalTotal());
  }

  private double roundTwoDecimals(Double value, int places) {
    if (places < 0) {
      throw new IllegalArgumentException();
    }

    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

  private void initRunningSubtotal() {
    runningSubTotal = 0.0;

    for (int i = 0; i < userCartItemEntries.size(); i++) {
      GroceryItem temp = mHappyDao.getGroceryItemById(userCartItemEntries.get(i).getGroceryItemId());

      runningSubTotal = runningSubTotal + temp.getPrice() * userCartItemEntries.get(i).getHowManyGroceryItemInCart();
    }

    runningSubTotal = roundTwoDecimals(runningSubTotal, 2);
  }

  private void initRunningFinalTotal() {
    runningFinalTotal = + runningSubTotal + runningTax;

    runningFinalTotal = roundTwoDecimals(runningFinalTotal, 2);
  }

  private void initTaxes() {
    SharedPreferences sharedPreferences = context.getSharedPreferences("defaultTaxEntry", Context.MODE_PRIVATE);
    float hi = sharedPreferences.getFloat("tax", 0);
    runningTax = runningSubTotal * sharedPreferences.getFloat("tax", 0.1f);

    runningTax = roundTwoDecimals(runningTax, 2);
  }

  private String beautifyTaxes() {
    String[] splitCalculateSubtotal= runningTax.toString().split("[.]");
    StringBuilder beautifiedTax = new StringBuilder();

    beautifiedTax.append("$");

    if (splitCalculateSubtotal[1].length() == 1) {
      beautifiedTax.append(splitCalculateSubtotal[0]).append(".").append(splitCalculateSubtotal[1]).append("0 ");
    }
    else {
      beautifiedTax.append(splitCalculateSubtotal[0]).append(".").append(splitCalculateSubtotal[1]).append(" ");
    }

    beautifiedTax = new StringBuilder(beautifiedTax.substring(0, beautifiedTax.length() - 1));

    return beautifiedTax.toString();
  }

  private String beautifyFinalTotal() {
    String[] splitCalculateFinalTotal = runningFinalTotal.toString().split("[.]");
    StringBuilder beautifiedCalculateFinalTotal = new StringBuilder();

    beautifiedCalculateFinalTotal.append("$");

    if (splitCalculateFinalTotal[1].length() == 1) {
      beautifiedCalculateFinalTotal.append(splitCalculateFinalTotal[0]).append(".").append(splitCalculateFinalTotal[1]).append("0 ");
    }
    else {
      beautifiedCalculateFinalTotal.append(splitCalculateFinalTotal[0]).append(".").append(splitCalculateFinalTotal[1]).append(" ");
    }

    beautifiedCalculateFinalTotal = new StringBuilder(beautifiedCalculateFinalTotal.substring(0, beautifiedCalculateFinalTotal.length() - 1));

    return beautifiedCalculateFinalTotal.toString();
  }

  private String beautifySubTotal() {
    String[] splitCalculateSubtotal= runningSubTotal.toString().split("[.]");
    StringBuilder beautifiedCalculateSubtotal = new StringBuilder();

    beautifiedCalculateSubtotal.append("$");

    if (splitCalculateSubtotal[1].length() == 1) {
      beautifiedCalculateSubtotal.append(splitCalculateSubtotal[0]).append(".").append(splitCalculateSubtotal[1]).append("0 ");
    }
    else {
      beautifiedCalculateSubtotal.append(splitCalculateSubtotal[0]).append(".").append(splitCalculateSubtotal[1]).append(" ");
    }

    beautifiedCalculateSubtotal = new StringBuilder(beautifiedCalculateSubtotal.substring(0, beautifiedCalculateSubtotal.length() - 1));

    return beautifiedCalculateSubtotal.toString();
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