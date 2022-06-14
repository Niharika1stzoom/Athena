package com.firstzoom.athena.util;

import com.firstzoom.athena.model.User;
import com.google.gson.Gson;

import java.util.List;

public class GsonUtils {


    public static String getGsonObject(User user) {
        Gson gson = new Gson();
        return gson.toJson(user);
    }
    public static User getModelObjectUser(String user) {
        Gson gson = new Gson();
        return gson.fromJson(user, User.class);

    }

    public static String getGsonObjectList(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    public static List<String> getModelObjectList(String savedSet) {
        Gson gson = new Gson();
        return gson.fromJson(savedSet, List.class);
    }
}
