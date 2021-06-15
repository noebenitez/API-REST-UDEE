package com.utn.udee.model;

public enum UserType {
    EMPLOYEE("Employee"),
    CLIENT("Client");

    private String description;

    UserType(String description){
        this.description = description;
    }

    public static UserType find(final String value){
        for (UserType u : values()){
            if(u.toString().equalsIgnoreCase(value))
                return u;
        }
        throw new IllegalArgumentException(String.format("Invalid UserType: %s", value));
    }

    public String getDescription(){
        return description;
    }
}
