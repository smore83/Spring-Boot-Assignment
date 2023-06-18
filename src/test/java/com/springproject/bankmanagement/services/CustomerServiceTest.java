package com.springproject.bankmanagement.services;

import com.springproject.bankmanagement.dao.CustomerRepository;
import com.springproject.bankmanagement.entity.Customer;
import com.springproject.bankmanagement.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L, "John", "Doe"));
        customers.add(new Customer(2L, "Jane", "Smith"));
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        assertEquals(customers,result);
        verify(customerRepository,times(1)).findAll();

    }

    @Test
    void testGetCustomerById() {
        Customer customer = new Customer(1L, "John", "Doe");
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getCustomerById(1L);

        assertTrue(result.isPresent());
        assertEquals(customer, result.get());
        verify(customerRepository, times(1)).findById(1L);
    }


    @Test
    void testSaveCustomer() {
        Customer customer = new Customer(1L, "John", "Doe");
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.saveCustomer(customer);

        assertEquals(customer, result);
        verify(customerRepository,times(1)).save(customer);

    }

    @Test
    void testDeleteCustomer() {
        Long customerId = 1L;

        customerService.deleteCustomer(customerId);

        verify(customerRepository, times(1)).deleteById(customerId);
    }
}
