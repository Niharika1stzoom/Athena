package com.firstzoom.athena.login;

import androidx.annotation.Nullable;

import com.firstzoom.athena.model.User;


/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    @Nullable
    private User success;
    @Nullable
    private Integer error;

    public LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    public LoginResult(@Nullable User success) {
        this.success = success;
    }

    @Nullable
    User getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}