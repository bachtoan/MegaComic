package com.fpoly.ph25296.assignment.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ComicModel implements Serializable {
    private String _id;
    private String name;
    private String author;
    private String description;
    private String cover;
    private String publish;
    private String id_group;
    private ArrayList images;
    private ArrayList comment;

    public ComicModel(String name, String author, String description, String cover, String publish, String id_group, ArrayList images) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.cover = cover;
        this.publish = publish;
        this.id_group = id_group;
        this.images = images;
    }

    public ComicModel(String _id) {
        this._id = _id;
    }

    public ComicModel(String name, String author, String id_group, String description, String publish) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.id_group = id_group;
        this.publish = publish;
    }

    public ArrayList getComment() {
        return comment;
    }

    public void setComment(ArrayList comment) {
        this.comment = comment;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ComicModel(ArrayList images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getId_group() {
        return id_group;
    }

    public void setId_group(String id_group) {
        this.id_group = id_group;
    }

    public ArrayList getImages() {
        return images;
    }

    public void setImages(ArrayList images) {
        this.images = images;
    }
}
