package com.nqmgaming.assignment_minhnqph31902.ui;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.nqmgaming.assignment_minhnqph31902.ui.main.fragment.CalendarFragment;
import com.nqmgaming.assignment_minhnqph31902.ui.main.fragment.HomeFragment;
import com.nqmgaming.assignment_minhnqph31902.ui.main.fragment.SearchFragment;
import com.nqmgaming.assignment_minhnqph31902.ui.main.fragment.UserFragment;
import com.nqmgaming.assignment_minhnqph31902.preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    UserPreferences userPreferences;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavigation.setBackground(null);

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.user) {
                replaceFragment(new UserFragment());
            } else if (item.getItemId() == R.id.search) {
                replaceFragment(new SearchFragment());

            } else if (item.getItemId() == R.id.calendar) {
                replaceFragment(new CalendarFragment());
            }
            return true;
        });


        userPreferences = new UserPreferences(this);

        //swipe to refresh
        binding.swipeRefresh.setOnRefreshListener(() -> {
            //nếu như đang màn fragment nào thì sẽ refresh lại fragment đó
            if (binding.bottomNavigation.getSelectedItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (binding.bottomNavigation.getSelectedItemId() == R.id.user) {
                replaceFragment(new UserFragment());
            } else if (binding.bottomNavigation.getSelectedItemId() == R.id.search) {
                replaceFragment(new SearchFragment());

            } else if (binding.bottomNavigation.getSelectedItemId() == R.id.calendar) {
                replaceFragment(new CalendarFragment());
            }
            binding.swipeRefresh.setRefreshing(false);
        });
        Intent intent = getIntent();
        if (intent != null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_MAIN)) {
            // The activity was started from a notification, handle it here
            handleNotificationClick(intent);
        }

    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment instanceof UserFragment) {
            Bundle args = new Bundle();
            args.putInt("id", userPreferences.getIdUser());
            fragment.setArguments(args);
        }
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void handleNotificationClick(Intent intent) {
        // Check any additional data passed with the intent from the notification
        // You can use intent.getExtras() to retrieve any data if needed

        // For example, you can check if there's an extra named "notification_data"
        if (intent.hasExtra("notification_data")) {
            String notificationData = intent.getStringExtra("notification_data");

            // Handle the data or navigation based on the notification
            // For example, you can show a dialog or navigate to a specific fragment/activity
        }
    }
}
