package com.customer.shubham.serviceimpl;

import com.customer.shubham.dto.CustomerDTO;
import com.customer.shubham.dto.PageRequestDTO;
import com.customer.shubham.entity.Customer;
import com.customer.shubham.repository.CustomerRepository;
import com.customer.shubham.service.CustomerService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public String addCustomer(CustomerDTO customerDTO) {
        log.info("Enter into addCustomer");
        try {
            Customer customer = Customer.builder().firstName(customerDTO.getFirstName()).lastName(customerDTO.getLastName()).street(customerDTO.getStreet()).city(customerDTO.getCity()).email(customerDTO.getEmail()).phone(customerDTO.getPhone()).state(customerDTO.getState()).build();
            customerRepository.save(customer);
            return "Customer Record Saved Successfully !";
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Customer getCustomer(int id) {
        try {
            Customer customer = customerRepository.findById(id).get();
            return customer;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String updateCustomer(CustomerDTO customerDTO) {
        try {
            Customer customer = customerRepository.findById(customerDTO.getCustomerId()).get();
            customer.setCity(customerDTO.getCity());
            customer.setFirstName(customerDTO.getFirstName());
            customer.setLastName(customerDTO.getLastName());
            customer.setStreet(customerDTO.getStreet());
            customer.setState(customerDTO.getState());
            customer.setPhone(customerDTO.getPhone());
            customer.setEmail(customerDTO.getEmail());
            customerRepository.save(customer);
            return "Customer Record Updated Successfully for customerId: " + customerDTO.getCustomerId();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String deleteCustomer(int id) {

        try {
            customerRepository.deleteById(id);
            return "Customer Record Deleted Successfully for customerId: " + id;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Customer> getCustomerList(PageRequestDTO pageRequestDTO) {
        try {
            final Pageable page = getPageableInfo(pageRequestDTO);
             List<Customer> customers= customerRepository.findAll();
            return customers;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private Pageable getPageableInfo(PageRequestDTO dto) {
        final int pageNo = dto.getPageNo();
        final int pageSize = dto.getPageSize() != 0 ? dto.getPageSize() : 20;
        return getPageable(pageNo, pageSize, dto.getDirection(), dto.getProperty());
    }

    private Pageable getPageable( final int pageNo, final int pageSize,  final String direction,final String property) {
        return PageRequest.of(pageNo, pageSize, Sort.Direction.valueOf(direction), property);
    }

}
