package com.edynamix.learning.android.carservice.storages;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.edynamix.learning.android.carservice.models.CarOwner;
import com.edynamix.learning.android.carservice.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CarOwnersStorage {

    private List<CarOwner> carOwnersList;
    private Gson gson;
    private SharedPreferences sharedPreferences;

    public CarOwnersStorage(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.gson = new Gson();
        this.carOwnersList = readDataFromSharedPrefs();
    }

    public List<CarOwner> getCarOwners() {
        return this.carOwnersList;
    }

    public int getNextCarOwnerId() {
        if (carOwnersList.size() == 0) {
            return 1;
        }

        CarOwner lastCarOwner = carOwnersList.get(carOwnersList.size() - 1);
        return lastCarOwner.id + 1;
    }

    public boolean isCarOwnerWithIdInTheStorage(int carOwnerId) {
        for (CarOwner carOwner : carOwnersList) {
            if (carOwner.id == carOwnerId) {
                return true;
            }
        }

        return false;
    }

    public void addCarOwner(CarOwner carOwner) {
        carOwnersList.add(carOwner);
        storeDataToSharedPrefs();
    }

    public String serializeToJson() {
        return gson.toJson(carOwnersList);
    }

    public List<CarOwner> deserializeFromJson(String json) {
        Type typeOfT = TypeToken.getParameterized(List.class, CarOwner.class).getType();
        return new Gson().fromJson(json, typeOfT);
    }

    private void storeDataToSharedPrefs() {
        String json = serializeToJson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREFERENCES_JSON_CAR_OWNERS_KEY, json);
        editor.commit();
    }

    private List<CarOwner> readDataFromSharedPrefs() {
        String carOwnersKey = sharedPreferences.getString(Constants.SHARED_PREFERENCES_JSON_CAR_OWNERS_KEY, null);

        if (carOwnersKey != null) {
            List<CarOwner> list = deserializeFromJson(carOwnersKey);
            return list;
        }
        return new ArrayList<>();
    }
}
