package com.kdub.happydays;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class SearchItemHomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences preferences = getActivity().getSharedPreferences("session", MODE_PRIVATE);

        boolean isAdmin = preferences.getBoolean("isAdmin", false);

        View view = inflater.inflate(R.layout.fragment_search_item_home, container, false);

        EditText searchField = view.findViewById(R.id.search_field);
        ImageView backToHome = view.findViewById(R.id.back_button_mag_glass);

//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        // TODO: this is old, find something new if you have time
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//        searchField.setFocusable(true);
//        searchField.isFocusableInTouchMode();
//        searchField.requestFocus();

        backToHome.setOnClickListener(new View.OnClickListener() {
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



        return view;
    }
}