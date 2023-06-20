package com.springproject.bankmanagement.rest;

import com.springproject.bankmanagement.dto.CustomerDTO;
import com.springproject.bankmanagement.entity.Bank;
import com.springproject.bankmanagement.entity.Customer;
import com.springproject.bankmanagement.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bank_mgmt/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerController(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerDTO> customerDTOs = customers.stream()
                .map(this::entityToDTO)
                .toList();
        return new ResponseEntity<>(customerDTOs, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(value -> new ResponseEntity<>(entityToDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = dtoToEntity(customerDTO);
        Customer createdCustomer = customerService.saveCustomer(customer);
        CustomerDTO createdCustomerDTO = entityToDTO(createdCustomer);
        return new ResponseEntity<>(createdCustomerDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        Optional<Customer> existingCustomer = customerService.getCustomerById(id);
        if (existingCustomer.isPresent()) {
            Customer customer = dtoToEntity(customerDTO);
            customer.setId(id);
            Customer updatedCustomer = customerService.saveCustomer(customer);
            CustomerDTO updatedCustomerDTO = entityToDTO(updatedCustomer);
            return new ResponseEntity<>(updatedCustomerDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        Optional<Customer> existingCustomer = customerService.getCustomerById(id);
        if (existingCustomer.isPresent()) {
            // Remove the customer from associated banks
            for (Bank bank : existingCustomer.get().getBanks()) {
                bank.getCustomers().remove(existingCustomer.get());
            }
            customerService.deleteCustomer(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private Customer dtoToEntity(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }

    private CustomerDTO entityToDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }
}
