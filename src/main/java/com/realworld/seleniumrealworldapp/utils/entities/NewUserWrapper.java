package com.realworld.seleniumrealworldapp.utils.entities;

public class NewUserWrapper {
private NewUser user;

    public NewUserWrapper(NewUser user) {
        this.user = user;
    }

    public NewUser getUser() {
        return user;
    }

    public void setUser(NewUser user) {
        this.user = user;
    }
}
