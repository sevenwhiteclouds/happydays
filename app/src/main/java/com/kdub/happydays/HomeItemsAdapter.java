package com.kdub.happydays;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.kdub.happydays.db.AppDataBase;
import com.kdub.happydays.db.HappyDAO;

import java.util.List;
import java.util.Locale;

public class HomeItemsAdapter extends RecyclerView.Adapter<HomeItemsAdapter.MyViewHolder> {
  private Context context;
  private List<GroceryItem> groceryItems;
  private CartItem groceryItemToAddToCart;
  private SharedPreferences mPreferences;
  private HappyDAO mHappyDAO;

  public HomeItemsAdapter(Context context, List<GroceryItem> groceryItems) {
    this.context = context;
    this.groceryItems = groceryItems;
    mPreferences = context.getSharedPreferences("session", MODE_PRIVATE);
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
    holder.itemPicture.setImageResource(groceryItems.get(position).getItemImage());
    holder.itemName.setText(beautifyItemName(groceryItems.get(position).getName()));
    holder.itemDenomination.setText(beautifyItemQuantityAndDenomination(groceryItems.get(position).getQuantity(), groceryItems.get(position).getDenomination()));
    holder.itemPrice.setText(beautifyItemPrice(groceryItems.get(position).getPrice()));
    holder.itemEntire.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO: code to view the entire item here
        Toast.makeText(context, "entire item clicked", Toast.LENGTH_SHORT).show();
      }
    });

    if (mPreferences.getBoolean("isAdmin", false)) {
      holder.itemAddButton.setVisibility(View.GONE);
    }
    else {
      holder.itemAddButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          mHappyDAO = Room.databaseBuilder(context, AppDataBase.class, AppDataBase.DATABASE_NAME)
            .allowMainThreadQueries().build().happyDAO();

          groceryItemToAddToCart = new CartItem(mPreferences.getInt("userId", 0), groceryItems.get(position).getGroceryItemId());

          List<CartItem> allUserEntriesInCartDatabase = mHappyDAO.getCartItemsByUserId(mPreferences.getInt("userId", 0));

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
            mHappyDAO.insert(groceryItemToAddToCart);
          }
          else {
            // lmao these names suck
            allUserEntriesInCartDatabase.get(positionWhereItemExist).setHowManyGroceryItemInCart(allUserEntriesInCartDatabase.get(positionWhereItemExist).getHowManyGroceryItemInCart() + 1);
            mHappyDAO.update(allUserEntriesInCartDatabase.get(positionWhereItemExist));
          }

          Toast.makeText(context, "HappyDays! " + beautifyItemName(groceryItems.get(position).getName()) + " has been added to the cart", Toast.LENGTH_SHORT).show();
        }
      });
    }
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
    String[] splitDenomination = beautifyThisItemDenomination.split("\\s");

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
    RelativeLayout itemEntire;
    ImageView itemPicture;
    TextView itemName;
    TextView itemDenomination;
    TextView itemPrice;
    ImageView itemAddButton;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);

      itemEntire = itemView.findViewById(R.id.recycle_view_relative_layout);
      itemPicture = itemView.findViewById(R.id.recycle_item_picture);
      itemName = itemView.findViewById(R.id.recycle_item_name);
      itemDenomination = itemView.findViewById(R.id.recycle_item_denomination);
      itemPrice = itemView.findViewById(R.id.recycle_item_price);
      itemAddButton = itemView.findViewById(R.id.recycle_item_add_button);
    }
  }
}