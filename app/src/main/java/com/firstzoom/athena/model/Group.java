package com.firstzoom.athena.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Group {
    UUID id;
    String name;
    List<Item> image_files;
    List<Item> video_files;
    List<Item> all_files;
    String created_by,description;
    Date created_at;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Item> getImage_files() {
        return image_files;
    }

    public List<Item> getVideo_files() {
        return video_files;
    }

    public String getCreated_by() {
        return created_by;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public String getDescription() {
        return description;
    }

    public List<Item> getAll_files() {
        return all_files;
    }
}
