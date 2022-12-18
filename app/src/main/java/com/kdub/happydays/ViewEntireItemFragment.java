package com.kdub.happydays;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;

import java.util.List;
import java.util.Locale;


public class ViewEntireItemFragment extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_view_entire_item, container, false);

    HappyDAO happyDao = Room.databaseBuilder(getContext(), AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().happyDAO();

    SharedPreferences preferences = getActivity().getSharedPreferences("session", MODE_PRIVATE);
    SharedPreferences mPreferencesItemId = getActivity().getSharedPreferences("itemIdSave", MODE_PRIVATE);

    int itemId = mPreferencesItemId.getInt("itemId", 0);
    boolean isAdmin = preferences.getBoolean("isAdmin", false);

    Button backButton = view.findViewById(R.id.back_button_view_entire_item);
    Button addButton = view.findViewById(R.id.button_entire_item_add_to_cart);
    TextView itemName = view.findViewById(R.id.item_name_view_item);
    ImageView itemImage = view.findViewById(R.id.item_picture_entire_item);

    String name = beautifyItemName(happyDao.getGroceryItemById(itemId).getName());

    itemName.setText(name);

    itemImage.setImageResource(happyDao.getGroceryItemById(itemId).getItemImage());

    backButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FragmentTransaction fragment = getActivity().getSupportFragmentManager().beginTransaction();
        if (isAdmin) {
          fragment.replace(R.id.frame_layout_admin, new HomeFragment());
        }
        else {
          fragment.replace(R.id.frame_layout_normal, new HomeFragment());
        }
        fragment.commit();
      }
    });

    if (!isAdmin) {
      addButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          CartItem groceryItemToAddToCart = new CartItem(preferences.getInt("userId", 0), itemId);

          List<CartItem> allUserEntriesInCartDatabase = happyDao.getCartItemsByUserId(preferences.getInt("userId", 0));

          boolean itemExists = false;
          int positionWhereItemExist = 0;

          for (int i = 0; i < allUserEntriesInCartDatabase.size(); i++) {
            if (allUserEntriesInCartDatabase.get(i).equals(groceryItemToAddToCart)) {
              itemExists = true;
              positionWhereItemExist = i;
              break;
            }
          }

          if (!itemExists) {
            happyDao.insert(groceryItemToAddToCart);
          }
          else {
            // lmao these names suck
            allUserEntriesInCartDatabase.get(positionWhereItemExist).setHowManyGroceryItemInCart(allUserEntriesInCartDatabase.get(positionWhereItemExist).getHowManyGroceryItemInCart() + 1);
            happyDao.update(allUserEntriesInCartDatabase.get(positionWhereItemExist));
          }

          Toast.makeText(getContext(), "HappyDays! " + beautifyItemName(happyDao.getGroceryItemById(itemId).getName()) + " has been added to the cart", Toast.LENGTH_SHORT).show();
        }
      });
    }
    else {
      String deleteButton = "Delete";
      addButton.setText(deleteButton);

      addButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          GroceryItem itemToDelete = happyDao.getGroceryItemById(itemId);
          Toast.makeText(getContext(), beautifyItemName(happyDao.getGroceryItemById(itemId).getName()) + " has been added delete.", Toast.LENGTH_SHORT).show();
          happyDao.delete(itemToDelete);

          FragmentTransaction fragment = getActivity().getSupportFragmentManager().beginTransaction();
          fragment.replace(R.id.frame_layout_admin, new HomeFragment());
          fragment.commit();
        }
      });
    }

    return view;
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
}