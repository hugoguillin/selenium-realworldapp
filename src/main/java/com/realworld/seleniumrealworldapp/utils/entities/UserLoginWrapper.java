package com.realworld.seleniumrealworldapp.utils.entities;

public class UserLoginWrapper {
    private UserLogin user;

    public UserLoginWrapper(UserLogin user) {
        this.user = user;
    }

    public UserLogin getUser() {
        return user;
    }

    public void setUser(UserLogin user) {
        this.user = user;
    }
}
