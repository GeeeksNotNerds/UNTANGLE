package com.android.sgms_20;


import java.util.List;

public class Posts
{
    private String name;
    private String email;
    private String description;
    private String date;
    private String time;
    private String uid;
    private String profileImage;
    private String mode;
    private String category;
    private String subCategory;
    private List<Tag> tags;

    public Posts(String name, String email, String description, String date, String time, String uid,String profileImage, String mode, String category, String subCategory,List<Tag> tags) {
        this.name = name;
        this.profileImage=profileImage;
        this.email = email;
        this.description = description;
        this.date = date;
        this.tags=tags;
        this.time = time;
        this.uid = uid;
        this.mode = mode;
        this.category = category;
        this.subCategory = subCategory;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setName(String name) {
        this.name = name;
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

        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getTags() != null ? getTags().hashCode() : 0);
        return result;
    }
}
