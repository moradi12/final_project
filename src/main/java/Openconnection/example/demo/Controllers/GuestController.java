package Openconnection.example.demo.Controllers;

import Openconnection.example.demo.Exceptions.CouponNotFoundException;
import Openconnection.example.demo.Exceptions.CustomerExceptionException;
import Openconnection.example.demo.Service.GuestService;
import Openconnection.example.demo.beans.Category;
import Openconnection.example.demo.beans.Coupon;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.util.List;
@RestController
@AllArgsConstructor
@RequestMapping("/api/guest")
public class GuestController {
    private final GuestService guestService;

    @GetMapping("/coupons/company/{companyId}")
    public List<Coupon> getCouponsByCompany(@PathVariable Integer companyId) throws CouponNotFoundException {
        return guestService.searchCouponsByCompany(companyId);
    }
//    @GetMapping("/coupon/title/{title}")
//    public Coupon getCouponByTitle(@PathVariable String title) throws CouponNotFoundException {
//        return guestService.searchCouponByTitle(title);
//    }

    @GetMapping("/coupons/expired")
    public List<Coupon> getExpiredCoupons(@RequestParam Date endDate) throws CouponNotFoundException {
        return guestService.searchExpiredCoupons(endDate);
    }

    @GetMapping("/coupons/category/{category}")
    public List<Coupon> getCouponsByCategory(@PathVariable Category category) throws CouponNotFoundException {
        return guestService.searchCouponsByCategory(category);
    }

    @GetMapping("/coupons/price-range")
    public List<Coupon> getCouponsByPriceRange(@RequestParam double minPrice, @RequestParam double maxPrice) throws CouponNotFoundException {
        return guestService.searchCouponsByPriceRange(minPrice, maxPrice);
    }

    @PostMapping("/purchase")
    public String purchaseCoupon(@RequestParam int couponId, @RequestParam int customerId) throws CouponNotFoundException, CustomerExceptionException {
        guestService.purchaseCoupon(couponId, customerId);
        return "Coupon purchased successfully";
    }
}



