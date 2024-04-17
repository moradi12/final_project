package Openconnection.example.demo.Repository;

import Openconnection.example.demo.beans.Company;
import Openconnection.example.demo.beans.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Boolean existsByEmailAndPassword(String email, String password);
    Optional<Company> findByName(String name);
    Company findByEmailAndPassword(String email, String password);
    Boolean existsByEmail(String email);

    Optional<Company> findById(int id);
    Boolean existsByIdNotAndId(int idNot, int id);


}
