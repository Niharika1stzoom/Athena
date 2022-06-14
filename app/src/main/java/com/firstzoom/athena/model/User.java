package com.firstzoom.athena.model;

import android.provider.ContactsContract;

import java.util.UUID;

public class User {
    UUID id;
    String email,name,token;
    Boolean is_exec;
    Organisation organisation;

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public Boolean getIs_exec() {
        return is_exec;
    }

    public Organisation getOrganisation() {
        return organisation;
    }
}
