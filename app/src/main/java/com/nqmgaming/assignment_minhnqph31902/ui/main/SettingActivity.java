package com.nqmgaming.assignment_minhnqph31902.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.nqmgaming.assignment_minhnqph31902.preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;

public class SettingActivity extends AppCompatActivity {
    Switch notificationSwitch;
    ImageButton btnBackSetting;
    private UserPreferences userPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        notificationSwitch = findViewById(R.id.notificationSwitch);

        btnBackSetting = findViewById(R.id.btnBackSetting);

        btnBackSetting.setOnClickListener(v -> {
            finish();
        });
        userPreferences = new UserPreferences(this);

        // Đặt trạng thái của Switch bằng trạng thái đã lưu
        notificationSwitch.setChecked(userPreferences.isNotificationEnabled());

        // Lắng nghe sự kiện thay đổi trạng thái của Switch
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            userPreferences.setNotificationEnabled(isChecked);
            if (isChecked) {
                Toast.makeText(this, "Notification is on", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notification is off", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
