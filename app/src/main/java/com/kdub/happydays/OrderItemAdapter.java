package com.kdub.happydays;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.MyViewHolder> {
  private Context context;
  private HappyDAO mHappyDao;
  private List<Order> userOrders;

  public OrderItemAdapter(Context context, List<Order> userOrders) {
    this.context = context;
    mHappyDao = Room.databaseBuilder(context, AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().happyDAO();
    this.userOrders = userOrders;
  }

  @NonNull
  @Override
  public OrderItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);

    View view = inflater.inflate(R.layout.order_item, parent, false);

    return new OrderItemAdapter.MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull OrderItemAdapter.MyViewHolder holder, int position) {
    String[] itemsSplit = userOrders.get(position).getItems().split(",");
    String[] amountSplit = userOrders.get(position).getAmountOfItems().split(",");


    if (itemsSplit.length == 1) {
      String itemOne = beautifyItemName(mHappyDao.getGroceryItemById(Integer.parseInt(itemsSplit[0])).getName()) +  " x " + beautifyItemQuantity(Double.valueOf(amountSplit[0]));
      holder.orderItemOne.setText(itemOne);
      holder.orderItemTwo.setVisibility(View.INVISIBLE);
      holder.orderItemThree.setVisibility(View.INVISIBLE);
    }
    else if (itemsSplit.length == 2) {
      String itemOne = beautifyItemName(mHappyDao.getGroceryItemById(Integer.parseInt(itemsSplit[0])).getName()) +  " x " + beautifyItemQuantity(Double.valueOf(amountSplit[0]));
      String itemTwo = beautifyItemName(mHappyDao.getGroceryItemById(Integer.parseInt(itemsSplit[1])).getName()) +  " x " + beautifyItemQuantity(Double.valueOf(amountSplit[1]));
      holder.orderItemOne.setText(itemOne);
      holder.orderItemTwo.setText(itemTwo);
      holder.orderItemThree.setVisibility(View.INVISIBLE);
    }
    else {
      String itemOne = beautifyItemName(mHappyDao.getGroceryItemById(Integer.parseInt(itemsSplit[0])).getName()) +  " x " + beautifyItemQuantity(Double.valueOf(amountSplit[0]));
      String itemTwo = beautifyItemName(mHappyDao.getGroceryItemById(Integer.parseInt(itemsSplit[1])).getName()) +  " x " + beautifyItemQuantity(Double.valueOf(amountSplit[1]));
      String itemThree = beautifyItemName(mHappyDao.getGroceryItemById(Integer.parseInt(itemsSplit[2])).getName()) +  " x " + beautifyItemQuantity(Double.valueOf(amountSplit[2]));
      holder.orderItemOne.setText(itemOne);
      holder.orderItemTwo.setText(itemTwo);
      holder.orderItemThree.setText(itemThree);
    }


    Integer totalItems = 0;

    for (int i = 0; i < amountSplit.length; i++) {
      totalItems += Integer.parseInt(amountSplit[i]);
    }

    String orderNumberString = "Order #" + userOrders.get(position).getOrderEntryId();
    holder.orderNumber.setText(orderNumberString);
    String totalItemInOrder = totalItems + " Items";
    holder.orderTotalItems.setText(totalItemInOrder);
    holder.orderTotal.setText(beautifyFinalTotal(userOrders.get(position).getTotalOrderPrice()));

    setStatusOfOrder(holder, position);

    holder.cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        userOrders.get(position).setOrderStatus(-1);
        mHappyDao.update(userOrders.get(position));
        setStatusOfOrder(holder, position);
      }
    });
  }


  private String beautifyItemQuantity(Double beautifyThisItemQuantity ) {
    String[] splitQuantity = beautifyThisItemQuantity.toString().split("[.]");

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

  private void setStatusOfOrder(OrderItemAdapter.MyViewHolder holder, int position) {
    if (userOrders.get(position).getOrderStatus() == -1) {
      String canceled ="Canceled";
      holder.orderStatusText.setTextColor(Color.parseColor("#FFF0484B"));
      holder.orderStatusText.setText(canceled);
      holder.orderStatus.setCardBackgroundColor(Color.parseColor("#40F0484B"));
    }
    else if (userOrders.get(position).getOrderStatus() == 0) {
      String pending ="Pending";
      holder.orderStatusText.setTextColor(Color.parseColor("#FFFDBD1A"));
      holder.orderStatusText.setText(pending);
      holder.orderStatus.setCardBackgroundColor(Color.parseColor("#40FDBD1A"));
    }
    else {
      String complete ="Completed";
      holder.orderStatusText.setTextColor(Color.parseColor("#FF3ED99A"));
      holder.orderStatusText.setText(complete);
      holder.orderStatus.setCardBackgroundColor(Color.parseColor("#403ED99A"));
    }
  }

  private String beautifyFinalTotal(Double total) {
    String roundedTotal = String.valueOf(roundTwoDecimals(total, 2));

    String[] splitCalculateFinalTotal = roundedTotal.toString().split("[.]");

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

  private double roundTwoDecimals(Double value, int places) {
    if (places < 0) {
      throw new IllegalArgumentException();
    }

    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

  @Override
  public int getItemCount() {
    return userOrders.size();
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {

    CardView entireOrderButton = itemView.findViewById(R.id.orders_card_view_recyle);
    TextView orderNumber = itemView.findViewById(R.id.orders_card_view_order_number);
    TextView storeNameOrCustomerName = itemView.findViewById(R.id.orders_card_view_store_name);
    TextView orderTotalItems = itemView.findViewById(R.id.orders_card_view_amount_items);
    TextView orderItemOne = itemView.findViewById(R.id.orders_card_view_item_one);
    TextView orderItemTwo = itemView.findViewById(R.id.orders_card_view_item_two);
    TextView orderItemThree = itemView.findViewById(R.id.orders_card_view_item_three);
    TextView orderTotal = itemView.findViewById(R.id.orders_card_view_total_order_price);
    CardView cancelButton = itemView.findViewById(R.id.orders_card_view_cancel);
    CardView orderStatus = itemView.findViewById(R.id.orders_card_view_order_status);
    TextView orderStatusText = itemView.findViewById(R.id.orders_card_view_order_status_text);


    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}