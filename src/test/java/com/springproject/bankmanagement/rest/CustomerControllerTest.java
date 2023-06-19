package com.springproject.bankmanagement.rest;

import com.springproject.bankmanagement.dto.CustomerDTO;
import com.springproject.bankmanagement.entity.Customer;
import com.springproject.bankmanagement.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllCustomers_ReturnsListOfCustomerDTOs() {
        List<Customer> customers = Arrays.asList(
                new Customer(1L, "John", "Doe"),
                new Customer(2L, "Jane", "Smith")
        );

        when(customerService.getAllCustomers()).thenReturn(customers);
        when(modelMapper.map(any(Customer.class), eq(CustomerDTO.class))).thenReturn(new CustomerDTO());

        ResponseEntity<List<CustomerDTO>> response = customerController.getAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getCustomerById_WithValidId_ReturnsCustomerDTO() {
        Long customerId = 1L;
        Customer customer = new Customer(customerId, "John", "Doe");
        CustomerDTO customerDTO = new CustomerDTO(customerId, "John", "Doe");
        when(customerService.getCustomerById(customerId)).thenReturn(Optional.of(customer));
        when(modelMapper.map(customer, CustomerDTO.class)).thenReturn(customerDTO);

        ResponseEntity<CustomerDTO> response = customerController.getCustomerById(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerDTO, response.getBody());
    }

    @Test
    void getCustomerById_WithInvalidId_ReturnsNotFound() {
        Long customerId = 1L;
        when(customerService.getCustomerById(customerId)).thenReturn(Optional.empty());

        ResponseEntity<CustomerDTO> response = customerController.getCustomerById(customerId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createCustomer_ReturnsCreatedCustomerDTO() {
        CustomerDTO customerDTO = new CustomerDTO(1L, "John", "Doe");
        Customer customer = new Customer(1L, "John", "Doe");
        when(modelMapper.map(customerDTO, Customer.class)).thenReturn(customer);
        when(customerService.saveCustomer(customer)).thenReturn(customer);
        when(modelMapper.map(customer, CustomerDTO.class)).thenReturn(customerDTO);

        ResponseEntity<CustomerDTO> response = customerController.createCustomer(customerDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(customerDTO, response.getBody());
    }

    @Test
    void updateCustomer_WithExistingCustomer_ReturnsUpdatedCustomerDTO() {
        Long customerId = 1L;
        CustomerDTO updatedCustomerDTO = new CustomerDTO(customerId, "Updated", "Customer");
        Customer updatedCustomer = new Customer(customerId, "Updated", "Customer");
        when(modelMapper.map(updatedCustomerDTO, Customer.class)).thenReturn(updatedCustomer);
        when(customerService.getCustomerById(customerId)).thenReturn(Optional.of(new Customer()));
        when(customerService.saveCustomer(updatedCustomer)).thenReturn(updatedCustomer);
        when(modelMapper.map(updatedCustomer, CustomerDTO.class)).thenReturn(updatedCustomerDTO);

        ResponseEntity<CustomerDTO> response = customerController.updateCustomer(customerId, updatedCustomerDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCustomerDTO, response.getBody());
    }

    @Test
    void updateCustomer_WithNonexistentCustomer_ReturnsNotFound() {
        Long customerId = 1L;
        CustomerDTO updatedCustomerDTO = new CustomerDTO(customerId, "Updated", "Customer");
        when(customerService.getCustomerById(customerId)).thenReturn(Optional.empty());

        ResponseEntity<CustomerDTO> response = customerController.updateCustomer(customerId, updatedCustomerDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteCustomer_WithExistingCustomer_ReturnsNoContent() {
        Long customerId = 1L;
        when(customerService.getCustomerById(customerId)).thenReturn(Optional.of(new Customer()));

        ResponseEntity<Void> response = customerController.deleteCustomer(customerId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(customerService, times(1)).deleteCustomer(customerId);
    }

    @Test
    void deleteCustomer_WithNonexistentCustomer_ReturnsNotFound() {
        Long customerId = 1L;
        when(customerService.getCustomerById(customerId)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = customerController.deleteCustomer(customerId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(customerService, never()).deleteCustomer(customerId);
    }
}
