package com.saltlux.deepsignal.web.service.dto;

import com.saltlux.deepsignal.web.domain.User;

public class UserImageDTO {

    private User user;
    private String imageBase64;

    public UserImageDTO() {}

    public UserImageDTO(User user, String imageBase64) {
        this.user = user;
        this.imageBase64 = imageBase64;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
