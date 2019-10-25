package com.edynamix.learning.android.carservice.storages;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import com.edynamix.learning.android.carservice.models.User;
import com.edynamix.learning.android.carservice.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UsersStorage {

    private Context context;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private List<User> userList;

    public UsersStorage(Context context) {
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.gson = new Gson();
        this.userList = readDataFromSharedPrefs();
    }

    public void addUser(User user) {
        userList.add(user);
        storeDataToSharedPrefs();
    }

    public boolean hasUserWithEmail(String email) {
        for (User user : userList) {
            if (user.email.equals(email)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public User getUserForEmail(String email) {
        for (User user : userList) {
            if (user.email.equals(email)) {
                return user;
            }
        }
        return null;
    }

    public String serializeToJson() {
        return gson.toJson(userList);
    }

    public List<User> deserializeFromJson(String json) {
        Type typeOfT = TypeToken.getParameterized(List.class, User.class).getType();
        return new Gson().fromJson(json, typeOfT);
    }

    private void storeDataToSharedPrefs() {
        String json = serializeToJson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREFERENCES_JSON_USERS_KEY, json);
        editor.commit();
    }

    private List<User> readDataFromSharedPrefs() {
        String usersKey = sharedPreferences.getString(Constants.SHARED_PREFERENCES_JSON_USERS_KEY, null);

        if (usersKey != null) {
            List<User> list = deserializeFromJson(usersKey);
            return list;
        }
        return new ArrayList<>();
    }

}
