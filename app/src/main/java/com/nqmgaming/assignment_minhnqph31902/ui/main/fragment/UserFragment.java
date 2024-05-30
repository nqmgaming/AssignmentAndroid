package com.nqmgaming.assignment_minhnqph31902.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nqmgaming.assignment_minhnqph31902.dao.TodoDAO;
import com.nqmgaming.assignment_minhnqph31902.dto.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.dao.UserDAO;
import com.nqmgaming.assignment_minhnqph31902.dto.UserDTO;
import com.nqmgaming.assignment_minhnqph31902.preferences.UserPreferences;

import com.nqmgaming.assignment_minhnqph31902.ui.authentication.AccountSettingActivity;
import com.nqmgaming.assignment_minhnqph31902.ui.main.SettingActivity;
import com.nqmgaming.assignment_minhnqph31902.ui.onboarding.GetStartActivity;


import java.util.ArrayList;

import io.github.cutelibs.cutedialog.CuteDialog;


public class UserFragment extends Fragment {
    int id;
    UserPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        if (args != null) {
            id = args.getInt("id");
        }
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imgUser = view.findViewById(R.id.imgUser);

        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tvDone = view.findViewById(R.id.tvDone);
        TextView tvNotDone = view.findViewById(R.id.tvNotDone);
        TextView tvSignOut = view.findViewById(R.id.tvSignout);
        TextView tvTotal = view.findViewById(R.id.tvTotal);
        TextView tvSetting = view.findViewById(R.id.tvSetting);
        TextView tvUsername = view.findViewById(R.id.tvUsername);
        TextView tvRateUs = view.findViewById(R.id.tvRateUs);
        TextView AboutUs = view.findViewById(R.id.tvAboutUs);

        CardView cvUser = view.findViewById(R.id.cvUser);

//
//        Glide.with(this)
//                .load(R.drawable.minh)
//                .transition(DrawableTransitionOptions.withCrossFade(500))
//                .transform(new CircleCrop())
//                .into(imgUser);
        UserDAO userDAO = new UserDAO(getContext());
        UserDTO userDTO = userDAO.getUserById(id);

        if (userDTO != null) {
            tvName.setText(userDTO.getFirstname() + " " + userDTO.getLastname());
        } else {
            // Handle the case when userDTO is null
            tvName.setText("Unknown User");
        }
        if (userDTO != null) {
            tvEmail.setText(userDTO.getEmail());
        } else {
            // Handle the case when userDTO is null
            tvEmail.setText("Unknown Email");
        }
        if (userDTO != null) {
            tvUsername.setText("@" + userDTO.getUsername());
        } else {
            // Handle the case when userDTO is null
            tvUsername.setText("Unknown Username");
        }
        TodoDAO todoDAO = new TodoDAO(getContext());
        ArrayList<TodoDTO> userDTOArrayList = todoDAO.getAllTodoByUserId(id);
        ArrayList<TodoDTO> doneItemsList = new ArrayList<>();
        ArrayList<TodoDTO> notDoneItemsList = new ArrayList<>();
        for (TodoDTO todoDTO : userDTOArrayList) {
            if (todoDTO.getStatus() == 1) {
                doneItemsList.add(todoDTO);
            } else {
                notDoneItemsList.add(todoDTO);
            }
        }
        if (notDoneItemsList.size() == 0) {
            tvDone.setText("Done: 0");
        } else {
            tvDone.setText("Done: " + notDoneItemsList.size() + "");
        }

        if (doneItemsList.size() == 0) {
            tvNotDone.setText("Not Done: 0");
        } else {
            tvNotDone.setText("Not Done: " + doneItemsList.size() + "");
        }

        if (userDTOArrayList.size() == 0) {
            tvTotal.setText("Total: 0");
        } else {
            tvTotal.setText("Total: " + userDTOArrayList.size() + "");
        }
        tvSignOut.setOnClickListener(v -> new CuteDialog.withAnimation(getContext())
                .setTitle("Sign out")
                .setAnimation(R.raw.logout)
                .hideCloseIcon(true)
                .setDescription("Are you sure you want to sign out?")
                .setNegativeButtonText("No", v1 -> {

                }).setPositiveButtonText("Yes", v12 -> {
                    preferences = new UserPreferences(requireContext());
                    preferences.logout();
                    Intent intent = new Intent(getContext(), GetStartActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .show());

        tvSetting.setOnClickListener(v -> {
            //Intent to Setting Activity
            Intent intent = new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
        });

        cvUser.setOnClickListener(v -> {
            //Intent to Setting Activity
            Intent intent = new Intent(getContext(), AccountSettingActivity.class);
            startActivity(intent);
        });
        tvRateUs.setOnClickListener(v -> {
          //intent google play
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse("https://play.google.com/store/apps/details?id=com.nqmgaming.assignment_minhnqph31902"));
            startActivity(intent);
        });
        AboutUs.setOnClickListener(v -> {
            //intent google play
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse("https://www.facebook.com/minh.nguyen.31902"));
            startActivity(intent);
        });
    }
}