package com.kdub.happydays;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;

public class OrdersFragment extends Fragment {
  private View view;
  private HappyDAO mHappyDAO;
  private SharedPreferences mPreferences;
  private int userId;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    mPreferences = getContext().getSharedPreferences("session", MODE_PRIVATE);
    mHappyDAO = Room.databaseBuilder(getActivity(), AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().happyDAO();
    userId = mPreferences.getInt("userId", 0);
    view = inflater.inflate(R.layout.fragment_orders, container, false);

    if (mHappyDAO.getOrdersByUserId(userId).size() == 0) {
      emptyOrders();
    }
    else {
      ordersNotEmpty();
      RecyclerView recyclerView = view.findViewById(R.id.orders_recycle_view);

      OrderItemAdapter adapter = new OrderItemAdapter(getContext(), mHappyDAO.getOrdersByUserIdDesc(userId));

      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      recyclerView.setAdapter(adapter);
    }

    return view;
  }

  private void emptyOrders() {
    view.findViewById(R.id.orders_no_order_emoji).setVisibility(View.VISIBLE);
    view.findViewById(R.id.orders_no_order_message).setVisibility(View.VISIBLE);
  }

  private void ordersNotEmpty() {
    view.findViewById(R.id.orders_header_text).setVisibility(View.VISIBLE);
    view.findViewById(R.id.orders_recycle_view).setVisibility(View.VISIBLE);
  }
}