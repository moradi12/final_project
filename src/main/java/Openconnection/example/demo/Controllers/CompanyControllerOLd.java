package Openconnection.example.demo.Controllers;

import Openconnection.example.demo.Exceptions.CompanyAlreadyExistsException;
import Openconnection.example.demo.Exceptions.CompanyNotFoundException;
import Openconnection.example.demo.Exceptions.CouponNotFoundException;
import Openconnection.example.demo.Service.CompanyService;
import Openconnection.example.demo.beans.Category;
import Openconnection.example.demo.beans.Company;
import Openconnection.example.demo.beans.Coupon;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyControllerOLd {
    private final CompanyService companyService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Company addCompany(@RequestBody Company company) throws CompanyAlreadyExistsException {
        companyService.addCompany(company);
        return company;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable int id) {
        try {
            companyService.deleteCompany(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Company deleted successfully");
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found");
        } catch (CouponNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete company");
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Company updateCompany(@PathVariable int id, @RequestBody Company company) throws CompanyNotFoundException {
        company.setId(id);
        companyService.updateCompany(company);
        return company;
    }


    @GetMapping("/all")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
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
    public List<Coupon> getCompanyCouponsByCategory(@PathVariable int id, @PathVariable Category category) throws CompanyNotFoundException {
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
    public void updateCoupon(@PathVariable int companyId, @PathVariable int couponId, @RequestBody Coupon coupon) throws CouponNotFoundException {
        coupon.setId(couponId);
        coupon.setCompanyId(companyId);
        companyService.updateCoupon(coupon);
    }

    @DeleteMapping("/{companyId}/coupons/{couponId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCoupon(@PathVariable int companyId, @PathVariable int couponId) throws CouponNotFoundException {
        companyService.deleteCoupon(couponId);
    }

    @GetMapping("/{companyId}/coupons/{couponId}")
    public Coupon getCouponById(@PathVariable int companyId, @PathVariable int couponId) throws CouponNotFoundException {
        return companyService.getCouponById(couponId);
    }
}