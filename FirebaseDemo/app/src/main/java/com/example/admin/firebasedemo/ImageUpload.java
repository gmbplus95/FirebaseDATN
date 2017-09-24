package com.example.admin.firebasedemo;

import java.io.Serializable;

/**
 * Created by Admin on 05-09-2017.
 */

public class ImageUpload implements Serializable {
    public String name;
    public String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageUpload() {
    }

    public ImageUpload(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
