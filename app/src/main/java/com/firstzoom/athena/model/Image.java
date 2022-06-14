package com.firstzoom.athena.model;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Image {
    String name;
    String createdAt;
    String imageUrl;
    UUID id;
    @SerializedName("url")
    String videoUrl;
    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public UUID getId() {
        return id;
    }
}
