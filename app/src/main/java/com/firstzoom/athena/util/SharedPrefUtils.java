package com.firstzoom.athena.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.firstzoom.athena.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SharedPrefUtils {
  synchronized public static void setSavedList(Context context,Set set) {
      removeSavedList(context);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        //editor.clear();
        editor.putStringSet(AppConstants.KEY_SAVED_LIST,set);
        editor.apply();
    }
    synchronized public static void removeSavedList(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        //editor.clear();
        editor.remove(AppConstants.KEY_SAVED_LIST);
        editor.apply();
    }

    public static Set getSavedList(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set savedSet = prefs.getStringSet(AppConstants.KEY_SAVED_LIST,new HashSet<String>() );
        Log.d(AppConstants.TAG,"Saved"+savedSet.size());
       return savedSet;
    }

    synchronized  public static void addPath(Context context,String path) {
        Set set=SharedPrefUtils.getSavedList(context);
        set.add(path);
        Log.d(AppConstants.TAG,"Added"+set.size());
        SharedPrefUtils.setSavedList(context,set);
    }
    synchronized public static void delPath(Context context,String path) {
        Set set=SharedPrefUtils.getSavedList(context);
        set.remove(path);
        Log.d(AppConstants.TAG,"Removed"+set.size());
        SharedPrefUtils.setSavedList(context,set);
    }
    synchronized public static void setUser(Context context, User customer) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(AppConstants.KEY_USER,GsonUtils.getGsonObject(customer));
        editor.apply();
    }

    public static User getUser(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String user = prefs.getString(AppConstants.KEY_USER, "");
        if(TextUtils.isEmpty(user))
            return null;
        else
            return GsonUtils.getModelObjectUser(user) ;
    }

    synchronized public static void delUser(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(AppConstants.KEY_USER);
        editor.apply();
    }
    public synchronized static void saveFcmToken(Context context,String token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(AppConstants.KEY_FCM_TOKEN,token);
        editor.apply();
    }
    public synchronized static String getFcmToken(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String token=prefs.getString(AppConstants.KEY_FCM_TOKEN,"");
        return token;
    }
}
