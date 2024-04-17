package Openconnection.example.demo.beans;

import lombok.Getter;

@Getter
public enum UserType {

    ADMIN("Admin"),
    CUSTOMER("Customer"),
    COMPANY("Company"),
    GUEST("Guest");

    private String roleName;

    UserType(String roleName) {
        this.roleName = roleName;
    }
}
