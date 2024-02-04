package com.customer.shubham.controller;

import com.azure.core.annotation.Get;
import com.customer.shubham.dto.CustomerDTO;
import com.customer.shubham.dto.PageRequestDTO;
import com.customer.shubham.entity.Customer;
import com.customer.shubham.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    CustomerService customerService;


    @GetMapping("/welcome")
     public String welcome(){
         return "Welcome to spring security app";
     }

    @PostMapping("/add")
    public String addCustomer(@RequestBody CustomerDTO customerDTO){
      return customerService.addCustomer(customerDTO);
    }

    @GetMapping("/getCustomer/{id}")

    public Customer addCustomer(@PathVariable int id){
        return customerService.getCustomer(id);
    }

    @PutMapping("/update/customer")
    public String updateCustomer(@RequestBody CustomerDTO customerDTO){
        return customerService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable int id){
        return customerService.deleteCustomer(id);
    }

    @PostMapping("/getCustomerList")
    public List<Customer> getCustomerList(@RequestBody PageRequestDTO pageRequestDTO){
        return customerService.getCustomerList(pageRequestDTO);
    }
}
