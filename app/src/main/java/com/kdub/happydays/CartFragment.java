package com.kdub.happydays;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;

import org.w3c.dom.Text;

import java.util.List;

public class CartFragment extends Fragment {
  private View view;
  private HappyDAO mHappyDAO;
  private SharedPreferences mPreferences;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    mPreferences = getContext().getSharedPreferences("session", MODE_PRIVATE);
    mHappyDAO = Room.databaseBuilder(getActivity(), AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().happyDAO();

    if (mHappyDAO.getCartItemsByUserId(mPreferences.getInt("userId", 0)).size() == 0) {
      view = inflater.inflate(R.layout.fragment_cart_empty, container, false);
    }
    else {
      view = inflater.inflate(R.layout.fragment_cart, container, false);

      // this is where we are going to call the cart item adapter
      // with the list of cart entries for the user
      RecyclerView recyclerView = view.findViewById(R.id.recycle_view_cart);

      TextView cartItemSubTotalText = view.findViewById(R.id.order_info_subtotal_money_amount);
      TextView cartItemTaxText = view.findViewById(R.id.order_info_tax_money_amount);
      TextView cartItemFinalTotalText = view.findViewById(R.id.order_info_total_money_amount);

      CartItemAdapter adapter = new CartItemAdapter(cartItemSubTotalText, cartItemTaxText, cartItemFinalTotalText, getContext(), mHappyDAO.getCartItemsByUserId(mPreferences.getInt("userId", 0)));
      recyclerView.setAdapter(adapter);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

      CardView confirmButton = view.findViewById(R.id.confirm_order_card_view);

      confirmButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          // TODO: code that places the order
          Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
        }
      });
    }
    return view;
  }
}