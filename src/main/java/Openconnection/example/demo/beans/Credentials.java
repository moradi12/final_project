package Openconnection.example.demo.beans;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Credentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 40)
    private String userName;
    @Column(length = 40)
    private String userPass;
    @Column(length = 40)
    private String userEmail;
    @Column(length = 40)
    private String userType;


}
