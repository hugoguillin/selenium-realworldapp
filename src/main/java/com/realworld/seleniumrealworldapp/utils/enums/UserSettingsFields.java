package com.realworld.seleniumrealworldapp.utils.enums;

public enum UserSettingsFields {
    IMAGE("profile-image"),
    USERNAME("username"),
    BIO("bio"),
    EMAIL("email"),
    PASSWORD("password");

    private final String field;

    UserSettingsFields(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
