package com.example.finelall.model;

import java.io.Serializable;

public class FindFriends_model  implements Serializable {
    String Email,Image,Username;

    public String getEmail() {
        return Email;
    }

    public FindFriends_model() {
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public FindFriends_model(String email, String image, String username) {
        Email = email;
        Image = image;
        Username = username;
    }
}
