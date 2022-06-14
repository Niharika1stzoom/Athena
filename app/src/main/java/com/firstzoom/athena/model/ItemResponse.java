package com.firstzoom.athena.model;

import java.util.List;

public class ItemResponse {
    List<Item> files;
    List<Group> group;

    public List<Item> getFiles() {
        return files;
    }

    public List<Group> getGroup() {
        return group;
    }
}
