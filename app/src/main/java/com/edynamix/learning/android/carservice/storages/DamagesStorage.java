package com.edynamix.learning.android.carservice.storages;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import com.edynamix.learning.android.carservice.models.Damage;
import com.edynamix.learning.android.carservice.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DamagesStorage {

    private List<Damage> damagesList;
    private Gson gson;
    private SharedPreferences sharedPreferences;

    public DamagesStorage(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.gson = new Gson();
        this.damagesList = readDataFromSharedPrefs();
    }

    public void addDamage(Damage damage) {
        damagesList.add(damage);
        storeDataToSharedPrefs();
    }

    public void deleteDamageWithId(int id) {
        for (Damage damage : damagesList) {
            if (damage.id == id) {
                damagesList.remove(damage);
                break;
            }
        }
        storeDataToSharedPrefs();
    }

    @Nullable
    public Damage getDamageWithId(int id) {
        for (Damage damage : damagesList) {
            if (damage.id == id) {
                return damage;
            }
        }

        return null;
    }

    public int getNextDamageId() {
        if (damagesList.size() == 0) {
            return 1;
        }

        Damage lastDamage = damagesList.get(damagesList.size() - 1);
        return lastDamage.id + 1;
    }

    public List<Damage> deserializeFromJson(String json) {
        Type typeOfT = TypeToken.getParameterized(List.class, Damage.class).getType();
        return new Gson().fromJson(json, typeOfT);
    }

    public String serializeToJson() {
        return gson.toJson(damagesList);
    }

    private void storeDataToSharedPrefs() {
        String json = serializeToJson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREFERENCES_JSON_DAMAGES_KEY, json);
        editor.commit();
    }

    private List<Damage> readDataFromSharedPrefs() {
        String damagesKey = sharedPreferences.getString(Constants.SHARED_PREFERENCES_JSON_DAMAGES_KEY, null);

        if (damagesKey != null) {
            List<Damage> list = deserializeFromJson(damagesKey);
            return list;
        }
        return new ArrayList<>();
    }
}
