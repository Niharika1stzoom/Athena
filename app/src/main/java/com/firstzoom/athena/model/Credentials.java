package com.firstzoom.athena.model;

import com.google.gson.annotations.SerializedName;

public class Credentials
{
    public String email;
    public String password;
    @SerializedName("fcm_token")
    String fcmToken;

    public Credentials(String email, String password, String fcmToken) {
        this.email = email;
        this.password = password;
        this.fcmToken = fcmToken;
    }
    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;

    }
}