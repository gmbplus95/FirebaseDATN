package com.example.admin.firebasedemo;

import java.io.Serializable;

/**
 * Created by Admin on 05-09-2017.
 */

public class ImageUpload implements Serializable {
    public String name;
    public String url;
    public String description;
    public String email;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ImageUpload(String name, String url,String description,String email) {
        this.name = name;
        this.url = url;
        this.description=description;
        this.email=email;
    }
}
