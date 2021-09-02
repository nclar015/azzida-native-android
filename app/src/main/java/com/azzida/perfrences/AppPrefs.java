package com.azzida.perfrences;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class AppPrefs {

    private static final String Prefsname = "Azzida";
    private static final String PrefsnameNew = "AzzidaNew";

    public static final String KEY_User_ID = "id";
    public static final String KEY_User_FirstName = "firstName";
    public static final String KEY_User_LastName = "lastName";
    public static final String KEY_User_RefCode = "RefCode";
    public static final String KEY_User_ProfileImage = "profileImage";
    public static final String KEY_USER_LOGIN_STATUS = "login_status";
    public static final String KEY_USER_SIGNUP_ID = "Signup_Id";
    public static final String KEY_USER_LOGIN_DEMO = "login_demo";
    public static final String KEY_Loc_Chn = "loc";
    public static final String KEY_Balance = "nalance";
    public static final String KEY_StripeAccId= "AccountId";
    public static final String KEY_USER_Job_Id = "jobId";

    public static final String KEY_Job_ListerFee = "jobListerFee";
    public static final String KEY_Job_SeekerFee = "jobSeekerFee";
    public static final String KEY_BackgroundCheck = "BackgroundCheck";
    public static final String KEY_CancelJobFee = "CancelJobFee";

    public static final String KEY_PriceMin = "PriceMin";
    public static final String KEY_PriceMax = "PriceMax";
    public static final String KEY_Distance = "Distance";
    public static final String KEY_Switch = "Switch";
    public static final String KEY_Switch_Active = "SwitchActive";
    public static final String KEY_CategoryList = "CategoryList";

    public static final String KEY_CandidateId = "CandidateId";

    public static final String KEY_LAT = "Lat";
    public static final String KEY_LONG = "Long";

    public static final String KEY_DEVICE_ID = "device_ic";

    public static final String KEY_Azzida_Verified = "AzzidaVerified";

    public static final String KEY_First_Time_Apply = "Apply";


    public static final String KEY_USER_IsFirst = "isFirst";

    public static void clearPrefsdata(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(Prefsname,
                Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public static void clearPrefsdataNew(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PrefsnameNew,
                Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public static void setStringKeyvaluePrefs(Context ctx, String key,
                                              String value) {
        SharedPreferences prefs = ctx.getSharedPreferences(Prefsname,
                Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public static void setStringKeyvaluePrefsNew(Context ctx, String key,
                                              String value) {
        SharedPreferences prefs = ctx.getSharedPreferences(PrefsnameNew,
                Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public static String getStringKeyvaluePrefs(Context ctx, String key) {
        SharedPreferences prefs = ctx.getSharedPreferences(Prefsname,
                Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }


    public static String getStringKeyvaluePrefsNew(Context ctx, String key) {
        SharedPreferences prefs = ctx.getSharedPreferences(PrefsnameNew,
                Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }


    public static void setintKeyvaluePrefs(Context ctx, String key, int value) {
        SharedPreferences prefs = ctx.getSharedPreferences(Prefsname,
                Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();

    }

    public static int getintKeyvaluePrefs(Context ctx, String key) {
        SharedPreferences prefs = ctx.getSharedPreferences(Prefsname,
                Context.MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }

    public static void setBooleanKeyvaluePrefs(Context ctx, String key,
                                               boolean value) {
        SharedPreferences prefs = ctx.getSharedPreferences(Prefsname,
                Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();

    }

    public static boolean getBooleanKeyvaluePrefs(Context ctx, String key) {
        SharedPreferences prefs = ctx.getSharedPreferences(Prefsname,
                Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }


}
