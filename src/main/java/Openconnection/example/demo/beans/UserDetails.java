package Openconnection.example.demo.beans;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
///todo this is zeeeeeeevvvvv class!!!!!!!!!
@Data
@NoArgsConstructor
@Entity
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String password;
    private UserType userType;

    public UserDetails(int id, String email, String password, UserType userType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public UserDetails(String email, String password, UserType userType){
        this.email=email;
        this.password=password;
        this.userType=userType;
    }
}