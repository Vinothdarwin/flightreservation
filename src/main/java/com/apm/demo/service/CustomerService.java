package com.apm.demo.service;

import com.apm.demo.models.Customer;
import com.apm.demo.models.Role;
import com.apm.demo.repository.CustomerRepository;
import com.apm.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Service
public class CustomerService {

    private static final Random random = new Random();
    private CustomerRepository customerRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public CustomerService(CustomerRepository userRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
   
    public List<Customer> findAll(){
    	return customerRepository.findAll();
    }
    
    public Customer findUserByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer findUserByUserName(String userName) {
        return customerRepository.findByUserName(userName);
    }

    public Customer saveAdminUser(Customer customer) {
        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
        customer.setActive(true);
        Role userRole = roleRepository.findByRole("ADMIN");
        customer.setRoles(new HashSet<>(Arrays.asList(userRole)));
        return customerRepository.save(customer);
    }

    public Customer saveUser(Customer customer) {
        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
        customer.setActive(true);
        Role userRole = roleRepository.findByRole("USER");
        customer.setRoles(new HashSet<>(Arrays.asList(userRole)));
        return customerRepository.save(customer);
    }

    public int getCustomerCount() {
        return customerRepository.findAll().size();
    }

    public Customer getRandomCustomer() {
        List<Customer> allCustomers = customerRepository.findAll();
        if (allCustomers.isEmpty()) {
            return null;
        }
        return allCustomers.get(random.nextInt(allCustomers.size()));
    }

}