package com.example.buddyppb.data.datastore;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {
    private static final String PREFS_NAME = "user_pref";

    private static final String NAME = "name";
    private static final String FIRSTTIME = "isFirstTime";

    private final SharedPreferences preferences;

    public UserPreference(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setUser(UserModel value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME, value.getName());
        editor.putBoolean(FIRSTTIME, value.isFirstTime());
        editor.apply();
    }

    public UserModel getUser() {
        UserModel model = new UserModel();
        model.setName(preferences.getString(NAME, ""));
        model.setFirstTime(preferences.getBoolean(FIRSTTIME, true));
        return model;
    }

}