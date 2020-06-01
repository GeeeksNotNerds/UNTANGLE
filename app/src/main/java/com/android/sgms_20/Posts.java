package com.android.sgms_20;


import android.content.Context;

import java.util.List;

public class Posts
{
    private String username;
    private String email;
    private String likes;
    private String description;
    private String date;
    private String time;
    private String uid;
    private String profileImage;
    private String mode;
    private String category;
    private String subCategory;
    private String showInformation;
    private String postid;
    private String postImage;
    private String status;
    private List<Tag> tags;

    public Posts(String postid,String name, String email, String description, String date, String time, String uid, String mode,String postimage, String category, String subCategory,String showInformation,String status,List<Tag> tags) {
        this.postid=postid;
        this.username = name;

        this.postImage=postimage;

        this.email = email;
        this.description = description;
        this.date = date;
        this.tags=tags;
        this.time = time;
        this.uid = uid;
        this.mode = mode;
        this.category = category;
        this.subCategory = subCategory;
        this.showInformation=showInformation;
        this.status=status;

    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getStatus(){return status;}
    public String setStatus(){return status;}
    public String getUsername() {
        return username;
    }

    public String getPostImage(){return postImage;}

    public void setPostImage( String postImage) {
        this.postImage = postImage;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }


    public String getShowInformation() {
        return showInformation;
    }

    public void setShowInformation(String showInformation) {
        this.showInformation = showInformation;
    }

    public String getName() {
        return username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setName(String name) {
        this.username = name;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
    public boolean hasTag(String string) {
    for (Tag tag : tags) {
        if (tag.getText().equals(string)) {
            return true;
        }
    }

    return false;
}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Posts)) return false;

        Posts question = (Posts) o;

        if (getName() != null ? !getName().equals(question.getName()) : question.getName() != null)
            return false;
        if (getEmail() != null ? !getEmail().equals(question.getEmail()) : question.getEmail() != null)
            return false;
        if(getStatus()!= null ? !getStatus().equals(question.getStatus()) : question.getStatus() !=null)
            return false;
        if (getDate() != null ? !getDate().equals(question.getDate()) : question.getDate() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(question.getDescription()) : question.getDescription() != null)
            return false;
        return getTags() != null ? getTags().equals(question.getTags()) : question.getTags() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode(): 0);
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getTags() != null ? getTags().hashCode() : 0);
        return result;
    }
}