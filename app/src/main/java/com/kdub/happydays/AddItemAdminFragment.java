package com.kdub.happydays;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;

import java.util.List;
import java.util.Locale;


public class AddItemAdminFragment extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_item_admin, container, false);

    HappyDAO happyDAO = Room.databaseBuilder(getContext(), AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().happyDAO();
    EditText category = view.findViewById(R.id.text_field_category);
    EditText name = view.findViewById(R.id.text_field_name);
    EditText quantity = view.findViewById(R.id.text_field_quantity);
    EditText denomination = view.findViewById(R.id.text_field_denomination);
    EditText price = view.findViewById(R.id.text_field_price);
    Button addButton = view.findViewById(R.id.add_item_button);


    addButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String categoryToString = category.getText().toString();
        String nameToString = name.getText().toString();
        double quantityToString = Double.parseDouble(quantity.getText().toString());
        String denominationToString = denomination.getText().toString();
        double priceToString = Double.parseDouble(price.getText().toString());
        int itemImage = 0;

        categoryToString = categoryToString.toLowerCase(Locale.ROOT);

        if (categoryToString.equals("produce")) {
          itemImage = R.drawable.produce;

        }
        else if (categoryToString.equals("bread")) {
          itemImage = R.drawable.bread;
        }
        else if (categoryToString.equals("meat")) {
          itemImage = R.drawable.meat;
        }
        else if (categoryToString.equals("dairy")) {
          itemImage = R.drawable.dairy;
        }
        else if (categoryToString.equals("frozen goods")) {
          itemImage = R.drawable.frozen;
        }
        else if (categoryToString.equals("canned goods")) {
          itemImage = R.drawable.canned_food;
        }
        else if (categoryToString.equals("beverages")) {
          itemImage = R.drawable.beverages;
        }
        else if (categoryToString.equals("baking goods")) {
          itemImage = R.drawable.bake;
        }
        else if (categoryToString.equals("cleaners")) {
          itemImage = R.drawable.cleaners;
        }
        else if (categoryToString.equals("paper goods")) {
          itemImage = R.drawable.paper_goods;
        }
        else if (categoryToString.equals("personal care")) {
          itemImage = R.drawable.personal_hygiene;
        }
        else {
          itemImage = R.drawable.other;
        }

        GroceryItem item = new GroceryItem(categoryToString, nameToString, quantityToString, denominationToString, priceToString, itemImage);

        List<GroceryItem> all = happyDAO.getAllGroceryItems();
        boolean itemExist = false;
        int placeWhereItemExist = 0;

        for (int i = 0; i < all.size(); i++) {
          if (all.get(i).equals(item)) {
            placeWhereItemExist = i;
            itemExist = true;
            all.get(i).setAmountOfThisItem(all.get(i).getAmountOfThisItem() + 1);
          }
        }

        if (itemExist) {
          happyDAO.update(all.get(placeWhereItemExist));
        }
        else {
          happyDAO.insert(item);
        }

        category.setText("");
        name.setText("");
        quantity.setText("");
        denomination.setText("");
        price.setText("");
        Toast.makeText(getContext(), "HappyDays! The item has been added.", Toast.LENGTH_SHORT).show();
      }
    });
    return view;
  }
}