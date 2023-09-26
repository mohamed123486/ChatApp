package com.example.finelall.model;

public class group_model {
    String Image,Password,GroupName;

    public group_model() {
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public group_model(String image, String password, String groupName) {
        Image = image;
        Password = password;
        GroupName = groupName;
    }
}
