package com.nqmgaming.assignment_minhnqph31902.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    //create variables
    private static final String KEY_LOGIN_STATE = "key_login_state";
    private final SharedPreferences sharedPreferences;

    //create constructor
    public UserPreferences(Context context) {

        //call shared preferences
        sharedPreferences = context.getSharedPreferences("user_preferences",
                Context.MODE_PRIVATE);

    }


    //is login if true and not login if false
    public boolean isLogin() {

        return sharedPreferences.getBoolean(KEY_LOGIN_STATE, false);

    }

    //set login state
    public void setLogin(boolean state) {

        sharedPreferences.edit()
                .putBoolean(KEY_LOGIN_STATE, state)
                .apply();

    }

    //logout
    public void logout() {

        sharedPreferences.edit()
                .clear()
                .apply();

    }

    //Save id user
    public void setIdUser(int id) {

        sharedPreferences.edit()
                .putInt("id_user", id)
                .apply();

    }

    //Get id user
    public int getIdUser() {

        return sharedPreferences.getInt("id_user", 0);

    }

    public void setFirstTime(boolean state) {

        sharedPreferences.edit()
                .putBoolean("first_time", state)
                .apply();

    }

    //get first time
    public boolean getFirstTime() {

        return sharedPreferences.getBoolean("first_time", true);

    }

    private static final String KEY_NOTIFICATION_STATE = "key_notification_state";

    // Kiểm tra trạng thái thông báo (đã bật hay tắt)
    public boolean isNotificationEnabled() {
        return sharedPreferences.getBoolean(KEY_NOTIFICATION_STATE, true);
    }

    // Đặt trạng thái thông báo (true: đã bật, false: đã tắt)
    public void setNotificationEnabled(boolean state) {
        sharedPreferences.edit()
                .putBoolean(KEY_NOTIFICATION_STATE, state)
                .apply();
    }
}
