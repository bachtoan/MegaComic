package com.fpoly.ph25296.assignment.model;

import java.io.Serializable;

public class UsersModel implements Serializable {
    private String name;
    private String _id;
    private String token;
    private String image;
    private String password;
    private String email;
    private String group;

    public UsersModel(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public UsersModel(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UsersModel{" +
                "name='" + name + '\'' +
                ", _id='" + _id + '\'' +
                ", token='" + token + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
