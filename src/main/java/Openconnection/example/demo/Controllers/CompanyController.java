package Openconnection.example.demo.Controllers;

import Openconnection.example.demo.Exceptions.CompanyNotFoundException;
import Openconnection.example.demo.Exceptions.CouponNotFoundException;
import Openconnection.example.demo.Service.CompanyService;
import Openconnection.example.demo.beans.Category;
import Openconnection.example.demo.beans.Company;
import Openconnection.example.demo.beans.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping("/add")
    public ResponseEntity<?> addCompany(@RequestBody Company company) {
        try {
            companyService.addCompany(company);
            return ResponseEntity.ok("Company added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<?> deleteCompany(@PathVariable int companyId) {
        try {
            companyService.deleteCompany(companyId);
            return ResponseEntity.ok("Company deleted successfully");
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (CouponNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/forceDelete/{companyId}")
    public ResponseEntity<?> forceDeleteCompany(@PathVariable int companyId) {
        try {
            companyService.forceDeleteCompany(companyId);
            return ResponseEntity.ok("Company and all associated coupons forcefully deleted");
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCompanies() {
        try {
            return ResponseEntity.ok(companyService.getAllCompanies());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving companies: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Company updateCompany(@PathVariable int id, @RequestBody Company company) throws
            CompanyNotFoundException {
        company.setId(id);
        companyService.updateCompany(company);
        return company;
    }

    @GetMapping("/{id}")
    public Optional<Company> getCompanyById(@PathVariable int id) throws CompanyNotFoundException {
        return companyService.getOneCompany(id);
    }

    @GetMapping("/{id}/coupons")
    public List<Coupon> getCompanyCouponsById(@PathVariable int id) throws CompanyNotFoundException {
        return companyService.getCompanyCoupons();
    }

    @GetMapping("/{id}/coupons/category/{category}")
    public List<Coupon> getCompanyCouponsByCategory(@PathVariable int id, @PathVariable Category category) throws
            CompanyNotFoundException {
        return companyService.getCompanyCouponsByCategory(id, category);
    }

    @PostMapping("/{id}/coupons")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCoupon(@PathVariable int id, @RequestBody Coupon coupon) throws CouponNotFoundException {
        coupon.setCompanyId(id);
        companyService.addCoupon(coupon);
    }

    @PutMapping("/{companyId}/coupons/{couponId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCoupon(@PathVariable int companyId, @PathVariable int couponId,
                             @RequestBody Coupon coupon) throws CouponNotFoundException {
        coupon.setId(couponId);
        coupon.setCompanyId(companyId);
        companyService.updateCoupon(coupon);
    }

    @DeleteMapping("/{companyId}/coupons/{couponId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteCoupon(@PathVariable int companyId, @PathVariable int couponId) {
        try {
            companyService.deleteCoupon(couponId);
            return ResponseEntity.ok("Coupon deleted successfully");
        } catch (CouponNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{companyId}/coupons/{couponId}")
    public Coupon getCouponById(@PathVariable int companyId, @PathVariable int couponId) throws
            CouponNotFoundException {
        return companyService.getCouponById(couponId);
    }
}
