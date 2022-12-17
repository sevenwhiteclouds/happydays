package com.kdub.happydays;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.List;
import java.util.Locale;

public class ManageUsersAdapterAdmin extends RecyclerView.Adapter<ManageUsersAdapterAdmin.MyViewHolder> {
  private Context context;
  private HappyDAO mHappyDao;
  private List<User> mUsers;
  private SharedPreferences mPreferences;
  private int userId;
  private HappyDAO mHappyDAO;

  public ManageUsersAdapterAdmin(Context context) {
    this.context = context;
    mHappyDao = Room.databaseBuilder(context, AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().happyDAO();
    this.mPreferences = context.getSharedPreferences("session", 0);
    userId = mPreferences.getInt("userId", 0);
    this.mUsers =  mHappyDao.getAllUsersExcept(userId);
    this.mHappyDAO = Room.databaseBuilder(context, AppDataBase.class, AppDataBase.DATABASE_NAME)
      .allowMainThreadQueries().build().happyDAO();
  }

  @NonNull
  @Override
  public ManageUsersAdapterAdmin.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);

    View view = inflater.inflate(R.layout.manage_users_user_template, parent, false);

    return new ManageUsersAdapterAdmin.MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ManageUsersAdapterAdmin.MyViewHolder holder, int position) {
    holder.userId.setText(beautifyUserId(position));
    holder.userName.setText(mUsers.get(position).getUserName());
    holder.firstName.setText(beautifyFirstName(position));
    holder.lastName.setText(beautifyLastName(position));
    setAccountTypeAndButton(holder, position);

    holder.accountTypeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        User user = mHappyDAO.getUserByUserId(mUsers.get(position).getUserId());
        if (user.getIsAdmin() == 1) {
          user.setIsAdmin(0);
          mUsers.get(position).setIsAdmin(0);
          Toast.makeText(context, mUsers.get(position).getUserName() + " has been set as normal.", Toast.LENGTH_LONG).show();
        }
        else {
          user.setIsAdmin(1);
          mUsers.get(position).setIsAdmin(1);
          Toast.makeText(context, mUsers.get(position).getUserName() + " has been set as an administrator.", Toast.LENGTH_LONG).show();
        }

        mHappyDAO.update(user);
        setAccountTypeAndButton(holder, position);
      }
    });

    holder.resetPassButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        User user = mHappyDAO.getUserByUserId(mUsers.get(position).getUserId());

        if (user.getPassword().equals("default")) {
          Toast.makeText(context, mUsers.get(position).getUserName() + "'s password has already been reset to default.", Toast.LENGTH_LONG).show();
        }
        else {
          user = mHappyDAO.getUserByUserId(mUsers.get(position).getUserId());
          user.setPassword("default");
          mHappyDAO.update(user);
          Toast.makeText(context, mUsers.get(position).getUserName() + "'s password has been set to default.", Toast.LENGTH_LONG).show();
        }
      }
    });

    holder.deleteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(context, mUsers.get(position).getUserName() + " has been deleted.", Toast.LENGTH_LONG).show();
        deleteAccount(mUsers.get(position).getUserId());
        mUsers.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mUsers.size());
      }
    });
  }

  private void deleteAccount(int userId) {
    mHappyDao.deleteAllCartEntriesByUserId(userId);
    mHappyDao.deleteAllOrdersByUserId(userId);
    mHappyDao.deleteUserByUserId(userId);
  }

  private void setAccountTypeAndButton(MyViewHolder holder, int position) {
    if (mUsers.get(position).getIsAdmin() == 1) {
      String normal = "Make Normal";
      String adminText = "Administrator";
      holder.makeAdminButtonText.setText(normal);
      holder.accountType.setText(adminText);
    }
    else {
      String admin = "Make Admin";
      String normalText = "Normal";
      holder.makeAdminButtonText.setText(admin);
      holder.accountType.setText(normalText);
    }
  }

  private String beautifyLastName(int position) {
    String last = mUsers.get(position).getLastName().toLowerCase(Locale.ROOT);

    last = last.substring(0, 1).toUpperCase() + last.substring(1);

    return "Last name: " + last;
  }

  private String beautifyFirstName(int position) {
    String first = mUsers.get(position).getFirstName().toLowerCase(Locale.ROOT);

    first = first.substring(0, 1).toUpperCase() + first.substring(1);

    return "First name: " + first;
  }

  private String beautifyUserId(int position) {
    int userId = mUsers.get(position).getUserId();

    return "User ID #" + userId;
  }


  private String beautifyNameAndUsername(String name, String username) {
    StringBuilder beautifiedNameUsername = new StringBuilder();

    name = name.toLowerCase(Locale.ROOT);
    name = name.substring(0, 1).toUpperCase() + name.substring(1);

    username = username.toLowerCase(Locale.ROOT);

    beautifiedNameUsername.append(name).append(" (").append(username).append(")");

    return beautifiedNameUsername.toString();
  }


  @Override
  public int getItemCount() {
    return mUsers.size();
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {

    TextView userId = itemView.findViewById(R.id.manage_users_recyle_admin_account_id);
    TextView userName = itemView.findViewById(R.id.manage_users_recyle_admin_user_name);
    TextView firstName = itemView.findViewById(R.id.manage_users_recyle_admin_first_name);
    TextView lastName = itemView.findViewById(R.id.manage_users_recyle_admin_last_name);
    TextView accountType = itemView.findViewById(R.id.manage_users_recyle_admin_account_type);
    CardView accountTypeButton = itemView.findViewById(R.id.manage_users_recyle_admin_make_admin_button);
    CardView resetPassButton = itemView.findViewById(R.id.manage_users_recyle_admin_reset_button);
    CardView deleteButton = itemView.findViewById(R.id.manage_users_recyle_admin_delete_button);

    TextView makeAdminButtonText = itemView.findViewById(R.id.manage_users_recyle_admin_make_admin_text);

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}