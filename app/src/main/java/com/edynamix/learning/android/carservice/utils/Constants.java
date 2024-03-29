package com.edynamix.learning.android.carservice.utils;

public interface Constants {

    public static final String EMPTY_VALUE = "";

    // Login page error dialog related constants.
    public static final int MAX_EMAIL_LENGTH = 50;
    public static final int MAX_PASSWORD_LENGTH = 30;

    // Dialog labels
    public static final String ERROR_TITLE = "Error";
    public static final String CLOSE = "Close";
    public static final String CONFIRM = "Confirm";

    // Shared preferences related constants.
    public static final String SHARED_PREFERENCES_LOGGED_IN_USER = "logged_in_user";
    public static final String SHARED_PREFERENCES_JSON_USERS_KEY = "users";
    public static final String SHARED_PREFERENCES_JSON_CARS_KEY = "cars";
    public static final String SHARED_PREFERENCES_JSON_CAR_OWNERS_KEY = "car_owners";
    public static final String SHARED_PREFERENCES_JSON_DAMAGES_KEY = "damages";

    // Intent extras constants
    public static final String EXTRA_CAR_ID = "car_id";
    public static final String EXTRA_DAMAGE_X_POS = "damage_x_pos";
    public static final String EXTRA_DAMAGE_Y_POS = "damage_y_pos";
    public static final String EXTRA_PHOTO_FILE_PATH = "photo_file_path";
}
