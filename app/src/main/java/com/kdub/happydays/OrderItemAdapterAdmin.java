package com.kdub.happydays;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

public class OrderItemAdapterAdmin extends RecyclerView.Adapter<OrderItemAdapterAdmin.MyViewHolder> {
  private Context context;
  private HappyDAO mHappyDao;
  private View view;
  private List<Order> userOrders;

  public OrderItemAdapterAdmin(View view, Context context, List<Order> userOrders) {
    this.view = view;
    this.context = context;
    mHappyDao = Room.databaseBuilder(context, AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().happyDAO();
    this.userOrders = userOrders;
  }

  @NonNull
  @Override
  public OrderItemAdapterAdmin.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);

    View view = inflater.inflate(R.layout.order_item_admin, parent, false);

    return new OrderItemAdapterAdmin.MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull OrderItemAdapterAdmin.MyViewHolder holder, int position) {
    User user = mHappyDao.getUserByUserId(userOrders.get(position).getUserId());

    holder.storeNameOrCustomerName.setText(beautifyNameAndUsername(user.getFirstName(), user.getUserName()));

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

    holder.completeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        userOrders.get(position).setOrderStatus(1);
        mHappyDao.update(userOrders.get(position));
        userOrders.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, userOrders.size());

        if (userOrders.size() == 0) {
          emptyOrders();
        }

        Toast.makeText(context, "Order completed", Toast.LENGTH_SHORT).show();
      }
    });

    holder.cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        userOrders.get(position).setOrderStatus(-1);
        mHappyDao.update(userOrders.get(position));
        userOrders.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, userOrders.size());

        if (userOrders.size() == 0) {
          emptyOrders();
        }

        Toast.makeText(context, "Order canceled", Toast.LENGTH_SHORT).show();
      }
    });
  }


  private void emptyOrders() {
    view.findViewById(R.id.orders_header_text_admin).setVisibility(View.GONE);
    view.findViewById(R.id.orders_recycle_view_admin).setVisibility(View.GONE);
    view.findViewById(R.id.orders_cleared_admin).setVisibility(View.VISIBLE);
    view.findViewById(R.id.orders_cleared_text_admin).setVisibility(View.VISIBLE);
    view.findViewById(R.id.orders_cleared_text_two_admin).setVisibility(View.VISIBLE);
    view.findViewById(R.id.orders_cleared_text_three_admin).setVisibility(View.VISIBLE);
  }

  private String beautifyNameAndUsername(String name, String username) {
    StringBuilder beautifiedNameUsername = new StringBuilder();

    name = name.toLowerCase(Locale.ROOT);
    name = name.substring(0, 1).toUpperCase() + name.substring(1);

    username = username.toLowerCase(Locale.ROOT);

    beautifiedNameUsername.append(name).append(" (").append(username).append(")");

    return beautifiedNameUsername.toString();
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

    CardView entireOrderButton = itemView.findViewById(R.id.orders_card_view_recyle_admin);
    TextView orderNumber = itemView.findViewById(R.id.orders_card_view_order_number_admin);
    TextView storeNameOrCustomerName = itemView.findViewById(R.id.orders_card_view_customer_info_name_admin);
    TextView orderTotalItems = itemView.findViewById(R.id.orders_card_view_amount_items_admin);
    TextView orderItemOne = itemView.findViewById(R.id.orders_card_view_item_one_admin);
    TextView orderItemTwo = itemView.findViewById(R.id.orders_card_view_item_two_admin);
    TextView orderItemThree = itemView.findViewById(R.id.orders_card_view_item_three_admin);
    TextView orderTotal = itemView.findViewById(R.id.orders_card_view_total_order_price_admin);
    CardView cancelButton = itemView.findViewById(R.id.orders_card_view_cancel_admin);
    CardView completeButton = itemView.findViewById(R.id.orders_card_view_complete_admin);


    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}