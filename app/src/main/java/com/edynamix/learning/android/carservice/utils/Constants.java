package com.edynamix.learning.android.carservice.utils;

public interface Constants {

    public static final char SPACE = ' ';
    public static final char FULL_STOP = '.';

    public static final String EMPTY_VALUE = "";

    // Login page error dialog related constants.
    public static final int MAX_EMAIL_LENGTH = 50;
    public static final int MAX_PASSWORD_LENGTH = 30;

    // Error dialog keys
    public static final String ERROR_TITLE = "Error";
    public static final String CLOSE = "Close";

    // Shared preferences related constants.
    public static final String SHARED_PREFERENCES_LOGGED_IN_USER = "logged_in_user";
    public static final String SHARED_PREFERENCES_JSON_USERS_KEY = "users";
    public static final String SHARED_PREFERENCES_JSON_CARS_KEY = "cars";
    public static final String SHARED_PREFERENCES_JSON_CAR_OWNERS_KEY = "car_owners";

    // Intent extras constants
    public static final String EXTRA_CAR_ID = "car_id";
    public static final String EXTRA_DAMAGE_ID = "damage_id";


}
