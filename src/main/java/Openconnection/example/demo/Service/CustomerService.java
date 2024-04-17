package Openconnection.example.demo.Service;

import Openconnection.example.demo.Exceptions.*;
import Openconnection.example.demo.Repository.CouponRepository;
import Openconnection.example.demo.Repository.CustomerRepository;
import Openconnection.example.demo.beans.Coupon;
import Openconnection.example.demo.beans.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService  {

    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;
    private final CouponService couponService;

    public boolean isCustomerExists(String email, String password) throws CustomerExceptionException {
        if (!customerRepository.existsByEmailAndPassword(email, password)) {
            throw new CustomerExceptionException(ErrMsg.AUTHENTICATION_FAILED);
        }
        return true;
    }

    public boolean isCustomerExistsByEmail(String email) throws CustomerExceptionException {
        return customerRepository.existsByEmail(email);
    }

    public void addCustomer(Customer customer) throws CustomerExceptionException {
        if (customerRepository.existsById(customer.getCustomerID()) || customerRepository.existsByEmail(customer.getEmail())) {
            throw new CustomerExceptionException(ErrMsg.CUSTOMER_ALREADY_EXISTS);
        }
        customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public void updateCustomer(Customer customer) throws CustomerExceptionException {
        int id = customer.getCustomerID();
        if (!customerRepository.existsById(id)) {
            throw new CustomerExceptionException(ErrMsg.CUSTOMER_NOT_FOUND);
        }
        Optional<Customer> existingCustomerByEmail = customerRepository.findByEmail(customer.getEmail());
        if (existingCustomerByEmail.isPresent() && existingCustomerByEmail.get().getCustomerID() != id) {
            throw new CustomerExceptionException(ErrMsg.CUSTOMER_ALREADY_EXISTS);
        }
        customerRepository.saveAndFlush(customer);

        System.out.println("Customer updated" + customer);
    }



    public void deleteCustomer(int customerID) throws CustomerExceptionException {
        if (!customerRepository.existsById(customerID)) {
            throw new CustomerExceptionException(ErrMsg.CUSTOMER_NOT_FOUND);
        }

        System.out.println("Customer exists. Deleting customer with ID: " + customerID);

        customerRepository.deleteById(customerID);
        System.out.println("Customer deleted successfully: " + customerID);
    }




///////////////// good 1
//    public void deleteCustomer(int customerID) throws CustomerExceptionException {
//        if (!customerRepository.existsById(customerID)) {
//            throw new CustomerExceptionException(ErrMsg.CUSTOMER_NOT_FOUND);
//        }
//        customerRepository.deleteById(customerID);
//        System.out.println("Customer deleted " + customerID);
//    }

    public Optional<Customer> getOneCustomer(int customerID) throws CustomerExceptionException {
        Optional<Customer> customer = customerRepository.findById(customerID);
        if (customer.isEmpty()) {
            throw new CustomerExceptionException(ErrMsg.CUSTOMER_NOT_FOUND);
        }
        return customer;
    }

    public Optional<Customer> findById(int id) throws CustomerExceptionException {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            throw new CustomerExceptionException(ErrMsg.CUSTOMER_NOT_FOUND);
        }
        return customer;
    }

    public boolean Login(String email, String password) throws AdminException, CompanyNotFoundException, CustomerExceptionException {
        Optional<Customer> customerOptional = customerRepository.findByEmailAndPassword(email, password);
        if (customerOptional.isPresent()) {
            return true;
        } else {
            throw new CustomerExceptionException(ErrMsg.AUTHENTICATION_FAILED);
        }
    }

    public void CouponPurchase(int couponID, int customerID) throws CouponNotFoundException, CouponOutOfStockException, CustomerExceptionException {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerExceptionException(ErrMsg.CUSTOMER_NOT_FOUND);
        }
        Optional<Coupon> optionalCoupon = couponRepository.findById(couponID);
        if (optionalCoupon.isEmpty()) {
            throw new CouponNotFoundException(ErrMsg.COUPON_NOT_FOUND.getMsg());
        }
        Customer customer = optionalCustomer.get();
        Coupon coupon = optionalCoupon.get();
        couponService.addCouponPurchase(couponID, customerID);
    }

    public List<Coupon> getCustomerCoupons(int customerID) throws CustomerExceptionException {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerExceptionException(ErrMsg.CUSTOMER_NOT_FOUND);
        }

        Customer customer = optionalCustomer.get();
        List<Coupon> coupons = customer.getCoupons();
        if (coupons.isEmpty()) {
            throw new CustomerExceptionException(ErrMsg.COUPON_NOT_FOUND);
        }

        return coupons;
    }
}
