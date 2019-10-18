package com.edynamix.learning.android.carservice.storages;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.edynamix.learning.android.carservice.models.Car;
import com.edynamix.learning.android.carservice.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CarsStorage {

    private List<Car> carsList;
    private Gson gson;
    private SharedPreferences sharedPreferences;

    public CarsStorage(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.gson = new Gson();
        this.carsList = readDataFromSharedPrefs();
    }

    public void addCar(Car car) {
        carsList.add(car);
        storeDataToSharedPrefs();
    }

    public void deleteCar(int carIndex) {
        carsList.remove(carIndex);
        storeDataToSharedPrefs();
    }

    public List<Car> getCars() {
        return this.carsList;
    }

    public String serializeToJson() {
        return gson.toJson(carsList);
    }

    public List<Car> deserializeFromJson(String json) {
        Type typeOfT = TypeToken.getParameterized(List.class, Car.class).getType();
        return new Gson().fromJson(json, typeOfT);
    }

    private void storeDataToSharedPrefs() {
        String json = serializeToJson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREFERENCES_JSON_CARS_KEY, json);
        editor.commit();
    }

    private List<Car> readDataFromSharedPrefs() {
        String carsKey = sharedPreferences.getString(Constants.SHARED_PREFERENCES_JSON_CARS_KEY, null);

        if (carsKey != null) {
            List<Car> list = deserializeFromJson(carsKey);
            return list;
        }
        return new ArrayList<>();
    }
}
