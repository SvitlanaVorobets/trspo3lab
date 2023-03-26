package com.example.lab3.contollers;

import com.example.lab3.dao.CustomerDao;
import com.example.lab3.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerDao customerDao;

    @GetMapping("")
    public ResponseEntity<Iterable<Customer>> findAll() {
        List<Customer> customers = customerDao.findAll();
        if (customerDao.findAll().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable("id") long id) {
        Optional<Customer> customer = customerDao.findById(id);
        return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer newCustomer){
        Customer customer = new Customer();
        customer.setCustomerName(newCustomer.getCustomerName());
        customer.setEmail(newCustomer.getEmail());
        customer.setPhone(newCustomer.getPhone());
        customer.setAddress(newCustomer.getAddress());
        return new ResponseEntity<>(customerDao.save(customer), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long id, @RequestBody Customer bodyCustomer) {
        Optional<Customer> publisher = customerDao.findById(id);

        if (publisher.isPresent()) {
            Customer newCustomer = publisher.get();
            newCustomer.setCustomerName(bodyCustomer.getCustomerName());
            newCustomer.setEmail(bodyCustomer.getEmail());
            newCustomer.setPhone(bodyCustomer.getPhone());
            newCustomer.setAddress(bodyCustomer.getAddress());
            return new ResponseEntity<>(customerDao.save(newCustomer), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable("id") long id) {
        try {
            customerDao.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
