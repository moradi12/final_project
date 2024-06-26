package Openconnection.example.demo.Repository;

import Openconnection.example.demo.beans.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {
    UserDetails findByEmail(String email);
}
