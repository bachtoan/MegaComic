package com.fpoly.ph25296.assignment.model;

public class CommentModel {
    String _id;
    String comicId;
    String userId;
    String name;
    String content;
    String date;

    String image;

    public CommentModel(String comicId, String userId, String name, String content, String image) {
        this.comicId = comicId;
        this.userId = userId;
        this.name = name;
        this.content = content;
        this.image = image;
    }

    public CommentModel(String _id, String comicId) {
        this._id = _id;
        this.comicId = comicId;
    }

    public CommentModel(String _id, String comicId, String userId, String name, String content, String date, String image) {
        this._id = _id;
        this.comicId = comicId;
        this.userId = userId;
        this.name = name;
        this.content = content;
        this.date = date;
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
