package Openconnection.example.demo.Service;

import Openconnection.example.demo.Exceptions.CouponNotFoundException;
import Openconnection.example.demo.Exceptions.CouponOutOfStockException;
import Openconnection.example.demo.Exceptions.CustomerExceptionException;
import Openconnection.example.demo.Exceptions.ErrMsg;
import Openconnection.example.demo.Repository.CustomerRepository;
import Openconnection.example.demo.beans.Coupon;
import Openconnection.example.demo.beans.Category;
import Openconnection.example.demo.Repository.CouponRepository;
import Openconnection.example.demo.beans.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@RequiredArgsConstructor

public class GuestService {

    private final CouponRepository couponRepository;
    private final CustomerRepository customerRepository;

    private final CouponService couponService;

    public void purchaseCoupon(int couponID, int customerID)
            throws CouponNotFoundException, CustomerExceptionException {
        Customer customer = customerRepository.findById(customerID)
                .orElseThrow(() -> new CustomerExceptionException(ErrMsg.CUSTOMER_NOT_FOUND));

        Coupon coupon = couponRepository.findById(couponID)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with ID" + couponID));
        try {
            couponService.addCouponPurchase(couponID, customerID);
        } catch (CouponOutOfStockException e) {
            throw new CouponNotFoundException("Coupon is out of stock" + couponID);
        }
    }
    public List<Coupon> searchCouponsByCompany(Integer companyId) throws CouponNotFoundException {
        List<Coupon> coupons = couponRepository.findByCompanyId(companyId);
        if (coupons.isEmpty()) {
            throw new CouponNotFoundException("No coupons found for company ID" + companyId);
        }
        return coupons;
    }

    public Coupon searchCouponByTitle(String title) throws CouponNotFoundException {
        Coupon coupon = couponRepository.findByTitle(title);
        if (coupon == null) {
            throw new CouponNotFoundException("Coupon not found with title" + title);
        }
        return coupon;
    }

    public boolean doesCouponExist(String title, int companyId) {
        return couponRepository.existsByTitleAndCompanyId(title, companyId);
    }

    public List<Coupon> searchExpiredCoupons(Date endDate) throws CouponNotFoundException {
        List<Coupon> coupons = couponRepository.findByEndDateBefore(endDate);
        if (coupons.isEmpty()) {
            throw new CouponNotFoundException("No expired coupons found before " + endDate);
        }
        return coupons;
    }

    public List<Coupon> searchCouponsByCategory(Category category) throws CouponNotFoundException {
        List<Coupon> coupons = couponRepository.findByCategory(category);
        if (coupons.isEmpty()) {
            throw new CouponNotFoundException("No coupons found for category" + category);
        }
        return coupons;
    }

    public List<Coupon> searchCouponsByPriceRange(double minPrice, double maxPrice) throws CouponNotFoundException {
        List<Coupon> coupons = couponRepository.findByPriceBetween(minPrice, maxPrice);
        if (coupons.isEmpty()) {
            throw new CouponNotFoundException("No coupons found in price range" + minPrice + " " + maxPrice);
        }
        return coupons;
    }
}

//    public List<Coupon> searchCouponsByDiscountPercentageAndAvailability(double minDiscount, double maxDiscount, boolean available) {
//        return couponRepository.findByDiscountPercentageBetweenAndAvailable(minDiscount, maxDiscount, available);
//    }}


