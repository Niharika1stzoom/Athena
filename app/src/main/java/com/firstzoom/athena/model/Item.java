package com.firstzoom.athena.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Item implements Serializable {
    UUID id;
    String description,name,upload_by,url,file,type,thumbnail;
    Double latitude,longitude;
    Date created_at;

    public UUID getId() {
        return id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getUpload_by() {
        return upload_by;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getFile() {
        return file;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
