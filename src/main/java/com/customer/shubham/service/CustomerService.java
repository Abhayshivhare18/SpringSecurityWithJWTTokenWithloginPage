package com.customer.shubham.service;

import com.customer.shubham.dto.CustomerDTO;
import com.customer.shubham.dto.PageRequestDTO;
import com.customer.shubham.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    String addCustomer(CustomerDTO customerDTO);

    Customer getCustomer(int id);

    String updateCustomer(CustomerDTO customerDTO);

    String deleteCustomer(int id);

    List<Customer> getCustomerList(PageRequestDTO pageRequestDTO);
}
