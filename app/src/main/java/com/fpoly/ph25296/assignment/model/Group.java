package com.fpoly.ph25296.assignment.model;

public class Group {
    private String _id;
    private String name;

    public Group(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public Group(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
