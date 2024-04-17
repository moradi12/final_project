package Openconnection.example.demo.Service;

import Openconnection.example.demo.Exceptions.*;
import Openconnection.example.demo.Repository.CompanyRepository;
import Openconnection.example.demo.Repository.CouponRepository;
import Openconnection.example.demo.Repository.CustomerRepository;
import Openconnection.example.demo.beans.Category;
import Openconnection.example.demo.beans.Company;
import Openconnection.example.demo.beans.Coupon;
import Openconnection.example.demo.beans.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;
    private int companyId;

    public boolean Login(String email, String password) throws AdminException, CompanyNotFoundException, CustomerExceptionException {
        Company company = companyRepository.findByEmailAndPassword(email, password);
        if (company == null) {
            throw new CompanyNotFoundException(ErrMsg.COMPANY_NOT_FOUND.getMsg());
        }
        this.companyId = company.getId();
        return true;

    }

    public void addCoupon(Coupon coupon) throws CouponNotFoundException {
        int id = coupon.getId();
        if (couponRepository.existsByTitleAndCompanyId(coupon.getTitle(), coupon.getCompanyId())) {
            throw new CouponNotFoundException(ErrMsg.COUPON_ID_ALREADY_EXISTS.getMsg());
        }
        couponRepository.save(coupon);
    }

    public Coupon getCouponById(int id) throws CouponNotFoundException {
        return couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(ErrMsg.COUPON_NOT_FOUND.getMsg() + ": " + id));
    }

    public void updateCoupon(Coupon coupon) throws CouponNotFoundException {
        if (!couponRepository.existsById(coupon.getId())) {
            throw new CouponNotFoundException(ErrMsg.COUPON_ID_NOT_FOUND.getMsg());
        }
        couponRepository.saveAndFlush(coupon);
    }

    public void deleteCoupon(int couponID) throws CouponNotFoundException {
        if (!couponRepository.existsById(couponID)) {
            throw new CouponNotFoundException(ErrMsg.COUPON_ID_NOT_FOUND.getMsg());
        }
        couponRepository.deleteById(couponID);
    }
///zzeeevvvv

    public List<Coupon> getCompanyCoupons() throws CompanyNotFoundException {
        ///return companyRepository.findById(this.companyId).get().getCoupons();
        return companyRepository.findById(this.companyId)
                .orElseThrow(() ->
                        new CompanyNotFoundException(ErrMsg.COMPANY_NOT_FOUND.getMsg())
                )
                .getCoupons();
        //throw new CompanyNotFoundException(ErrMsg.COMPANY_NOT_FOUND.getMsg());
    }

    public Coupon getOneCoupon(int couponID) throws CouponNotFoundException {
        return couponRepository.findById(couponID)
                .orElseThrow(() -> new CouponNotFoundException(ErrMsg.COUPON_ID_NOT_FOUND.getMsg()));
    }

    public List<Coupon> getCompanyCoupons(Category category) throws CompanyNotFoundException {
        Company company = companyRepository.findById(this.companyId)
                .orElseThrow(() -> new CompanyNotFoundException(ErrMsg.COMPANY_NOT_FOUND.getMsg()));
        List<Coupon> coupons = company.getCoupons().stream()
                .filter(coupon -> coupon.getCategory() == category)
                .collect(Collectors.toList());
        return coupons;
    }


    public List<Coupon> getCompanyCoupons(double MaxPrice) throws CompanyNotFoundException {
        throw new CompanyNotFoundException(ErrMsg.COMPANY_NOT_FOUND.getMsg());
    }


    public void getCompanyDetails() throws CompanyNotFoundException {
        Company company = companyRepository.findById(this.companyId)
                .orElseThrow(() -> new CompanyNotFoundException(ErrMsg.COMPANY_NOT_FOUND.getMsg()));
    }


    public boolean isCompanyExists(String email, String password) throws CompanyNotFoundException {
        if (!companyRepository.existsByEmailAndPassword(email, password)) {
            throw new CompanyNotFoundException(ErrMsg.COMPANY_NOT_FOUND.getMsg());
        }
        return true;
    }

    public boolean couponExistsByTitleAndCompany(String title, int companyId) {
        boolean exists = couponRepository.existsByTitleAndCompanyId(title, companyId);
        return exists;
    }


    public void deleteCouponPurchase(int couponID, int customerID) throws CouponNotFoundException {
        Coupon coupon = couponRepository.findById(couponID)
                .orElseThrow(() -> new CouponNotFoundException(ErrMsg.COUPON_NOT_FOUND.getMsg()));
        Optional<Customer> optionalCustomer = customerRepository.findById(customerID);

        if (optionalCustomer.isEmpty()) {
            throw new CouponNotFoundException(ErrMsg.CUSTOMER_NOT_FOUND.getMsg());
        }
        Customer customer = optionalCustomer.get();
        List<Coupon> customerCoupons = customer.getCoupons();
        customerCoupons.removeIf(c -> c.getId() == couponID);
        customer.setCoupons(customerCoupons);
        customerRepository.saveAndFlush(customer);
    }


    public void saveAndFlush(Coupon coupon) throws CouponNotFoundException {
        couponRepository.saveAndFlush(coupon);
    }


    public void addCouponPurchase(int couponID, int customerID) throws CouponNotFoundException {
        Coupon coupon = couponRepository.findById(couponID)
                .orElseThrow(() -> new CouponNotFoundException(ErrMsg.COUPON_NOT_FOUND.getMsg()));
        Optional<Customer> optionalCustomer = customerRepository.findById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CouponNotFoundException(ErrMsg.CUSTOMER_NOT_FOUND.getMsg());
        }
        Customer customer = optionalCustomer.get();
        List<Coupon> customerCoupons = customer.getCoupons();
        customerCoupons.add(coupon);
        customer.setCoupons(customerCoupons);
        customerRepository.saveAndFlush(customer);
        System.out.println("Coupon purchase added for Customer ID: " + customerID + " for Coupon ID: " + couponID);
    }

    public List<Coupon> getCompanyCouponsByCategory(int companyId, Category category) throws CompanyNotFoundException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(ErrMsg.COMPANY_NOT_FOUND.getMsg()));
        List<Coupon> coupons = company.getCoupons().stream()
                .filter(coupon -> coupon.getCategory() == category)
                .collect(Collectors.toList());
        return coupons;
    }


    public void deleteExpiredCoupons() throws CouponNotFoundException {
        Date currentDate = new Date(System.currentTimeMillis());
        try {
            List<Coupon> expiredCoupons = couponRepository.findByEndDateBefore(currentDate);
            for (Coupon coupon : expiredCoupons) {
                couponRepository.delete(coupon);
                System.out.println("Deleted expired coupon with ID " + coupon.getId());
            }
        } catch (Exception e) {
            throw new CouponNotFoundException("Error deleting expired coupons: " + e.getMessage());
        }
    }

    public List<Coupon> getExpiredCoupons(Date currentDate) throws CouponNotFoundException {
        return null;
    }


    public void addCompany(Company company) throws CompanyAlreadyExistsException {
        if (companyRepository.existsById(company.getId())) {
            throw new CompanyAlreadyExistsException(ErrMsg.COMPANY_ALREADY_EXISTS.getMsg());
        }
        companyRepository.save(company);
    }


    public void updateCompany(Company company) throws CompanyNotFoundException {
        if (!companyRepository.existsById(company.getId())) {
            throw new CompanyNotFoundException(ErrMsg.COMPANY_NOT_FOUND.getMsg());
        }
        companyRepository.saveAndFlush(company);
        System.out.println("Company updated: " + company);
    }

    //public void deleteCompany(int companyID) throws CompanyNotFoundException {
    //    if (!companyRepository.existsById(companyID)) {
    //        throw new CompanyNotFoundException(ErrMsg.COMPANY_NOT_FOUND.getMsg());
    //    }
    //    companyRepository.deleteById(companyID);
    //    System.out.println("Company deleted with ID: " + companyID);
    //}//


    public void deleteCompany(int companyId) throws CompanyNotFoundException, CouponNotFoundException {
//        Company company = companyRepository.findById(companyId)
//                .orElseThrow(() -> new CompanyNotFoundException(ErrMsg.COMPANY_NOT_FOUND.getMsg()));
//        List<Customer> customersWithCoupons = customerRepository.findByCouponsCompanyId(companyId);
//        if (!customersWithCoupons.isEmpty()) {
//            throw new CouponNotFoundException("Cannot delete company with ID " + companyId +
//                    ". Customers have purchased coupons from this company.");
//        }
//        List<Coupon> companyCoupons = couponRepository.findByCompanyId(companyId);
//        if (!companyCoupons.isEmpty()) {
//            throw new CouponNotFoundException("Cannot delete company with ID " + companyId +
//                    ". There are coupons associated with this company.");
//        }
        if (companyRepository.existsById(companyId)) {
            companyRepository.deleteById(companyId);
        } else {
            throw new CompanyNotFoundException(ErrMsg.COMPANY_NOT_FOUND.getMsg());
        }
        System.out.println("Company deleted with ID: " + companyId);
    }


    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }


    public Optional<Company> getOneCompany(int companyID) throws CompanyNotFoundException {
        Optional<Company> companyOptional = companyRepository.findById(companyID);
        if (companyOptional.isEmpty()) {
            throw new CompanyNotFoundException(ErrMsg.COMPANY_NOT_FOUND.getMsg());
        }
        return companyOptional;
    }


    public void saveAll(List<Company> companies) {
        companyRepository.saveAll(companies);
    }

}

