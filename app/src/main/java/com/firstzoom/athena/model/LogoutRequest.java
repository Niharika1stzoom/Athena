package com.firstzoom.athena.model;

import com.google.gson.annotations.SerializedName;

public class LogoutRequest {
    @SerializedName("fcm_token")
    String fcmToken;

    public LogoutRequest(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
