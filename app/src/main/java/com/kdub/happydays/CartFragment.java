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
  private int userId;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    mPreferences = getContext().getSharedPreferences("session", MODE_PRIVATE);
    mHappyDAO = Room.databaseBuilder(getActivity(), AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().happyDAO();
    userId = mPreferences.getInt("userId", 0);
    view = inflater.inflate(R.layout.fragment_cart, container, false);

    if (mHappyDAO.getCartItemsByUserId(userId).size() == 0) {
      emptyCart();
    }
    else {
      // this is where we are going to call the cart item adapter
      // with the list of cart entries for the user
      cartNotEmpty();
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
          submitOrder();
          Toast.makeText(getContext(), "HappyDays! Order submitted. Check orders tab for updates.", Toast.LENGTH_LONG).show();
        }
      });
    }
    return view;
  }

  private void submitOrder() {
    List<CartItem> groceryItemsInCart = mHappyDAO.getCartItemsByUserId(userId);
    StringBuilder itemsIds = new StringBuilder();
    StringBuilder amountOfItems = new StringBuilder();

    for (int i = 0; i < groceryItemsInCart.size(); i++) {
      itemsIds.append(groceryItemsInCart.get(i).getGroceryItemId()).append(",");
      amountOfItems.append(groceryItemsInCart.get(i).getHowManyGroceryItemInCart()).append(",");
    }
    itemsIds = new StringBuilder(itemsIds.substring(0, itemsIds.length() - 1));
    amountOfItems = new StringBuilder(amountOfItems.substring(0, amountOfItems.length() - 1));

    Order order = new Order(userId, itemsIds.toString(), amountOfItems.toString(), CartItemAdapter.runningFinalTotal);

    mHappyDAO.insert(order);

    view.findViewById(R.id.cart_header_text).setVisibility(View.GONE);
    view.findViewById(R.id.recycle_view_cart).setVisibility(View.GONE);
    view.findViewById(R.id.order_info_card_view).setVisibility(View.GONE);
    view.findViewById(R.id.confirm_order_card_view).setVisibility(View.GONE);

    view.findViewById(R.id.cart_order_placed).setVisibility(View.VISIBLE);
    view.findViewById(R.id.cart_order_placed_message).setVisibility(View.VISIBLE);

    mHappyDAO.deleteAllCartEntriesByUserId(userId);
  }

  private void emptyCart() {
    view.findViewById(R.id.cart_sad_face).setVisibility(View.VISIBLE);
    view.findViewById(R.id.cart_empty_message).setVisibility(View.VISIBLE);
  }

  private void cartNotEmpty() {
    view.findViewById(R.id.cart_header_text).setVisibility(View.VISIBLE);
    view.findViewById(R.id.recycle_view_cart).setVisibility(View.VISIBLE);
    view.findViewById(R.id.order_info_card_view).setVisibility(View.VISIBLE);
    view.findViewById(R.id.confirm_order_card_view).setVisibility(View.VISIBLE);
  }
}