package com.edynamix.learning.android.carservice.storages;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

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

    public void updateCar(int carIndex, Car newValue) {
        carsList.set(carIndex - 1, newValue); // Replace the current car at this index.
        storeDataToSharedPrefs();
    }

    public List<Car> getCars() {
        return this.carsList;
    }

    @Nullable
    public Car getCarWithId(int id) {
        for (Car car : carsList) {
            if (car.id == id) {
                return car;
            }
        }

        return null;
    }

    public int getNextCarId() {
        if (carsList.size() == 0) {
            return 1;
        }

        Car lastCar = carsList.get(carsList.size() - 1);
        return lastCar.id + 1;
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
