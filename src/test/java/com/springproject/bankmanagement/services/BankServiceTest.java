package com.springproject.bankmanagement.services;

import com.springproject.bankmanagement.dao.CustomerRepository;
import com.springproject.bankmanagement.dao.BankRepository;
import com.springproject.bankmanagement.dao.LocationRepository;
import com.springproject.bankmanagement.entity.Customer;
import com.springproject.bankmanagement.entity.Location;
import com.springproject.bankmanagement.service.BankService;
import com.springproject.bankmanagement.entity.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankServiceTest {

    @Mock
    private BankRepository bankRepository;

     @Mock
    private CustomerRepository authorRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private BankService bankService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBanks() {
        List<Bank> banks = new ArrayList<>();
        banks.add(new Bank(1L, "Bank 1", 2023, "ISBN-1", new HashSet<>(), new HashSet<>()));
        banks.add(new Bank(2L, "Bank 2", 2023, "ISBN-2", new HashSet<>(), new HashSet<>()));
        when(bankRepository.findAll()).thenReturn(banks);

        List<Bank> result = bankService.getAllBanks();

        assertEquals(2, result.size());
        assertEquals(banks, result);
        verify(bankRepository, times(1)).findAll();
    }

    @Test
    void testGetBankById() {
        Bank bank = new Bank(1L, "Bank 1", 2023, "ISBN-1", new HashSet<>(), new HashSet<>());
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(bank));

        Optional<Bank> result = bankService.getBankById(1L);

        assertTrue(result.isPresent());
        assertEquals(bank, result.get());
        verify(bankRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveBank() {
        Bank bank = new Bank(1L, "Bank 1", 2023, "ISBN-1", new HashSet<>(), new HashSet<>());
        when(bankRepository.save(bank)).thenReturn(bank);

        Bank result = bankService.saveBank(bank);

        assertEquals(bank, result);
        verify(bankRepository, times(1)).save(bank);
    }

    @Test
    void testDeleteBank() {
        Long bankId = 1L;

        bankService.deleteBank(bankId);

        verify(bankRepository, times(1)).deleteById(bankId);
    }

    @Test
    void testAssignCustomerToBank() {
        Long bankId = 1L;
        Long authorId = 1L;
        Bank bank = new Bank();
        Customer author = new Customer();
        Set<Customer> authorSet = new HashSet<>();
        authorSet.add(author);

        when(bankRepository.findById(bankId)).thenReturn(Optional.of(bank));
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(bankRepository.save(bank)).thenReturn(bank);

        Bank result = bankService.assignCustomerToBank(bankId, authorId);

        assertNotNull(result);
        assertEquals(authorSet, result.getCustomers());
        verify(bankRepository, times(1)).findById(bankId);
        verify(authorRepository, times(1)).findById(authorId);
        verify(bankRepository, times(1)).save(bank);
    }

    @Test
    void testAssignLocationToBank() {
        Long bankId = 1L;
        Long locationId = 1L;
        Bank bank = new Bank();
        Location location = new Location();
        Set<Location> locationSet = new HashSet<>();
        locationSet.add(location);

        when(bankRepository.findById(bankId)).thenReturn(Optional.of(bank));
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
        when(bankRepository.save(bank)).thenReturn(bank);

        Bank result = bankService.assignLocationToBank(bankId, locationId);

        assertNotNull(result);
        assertEquals(locationSet, result.getLocations());
        verify(bankRepository, times(1)).findById(bankId);
        verify(locationRepository, times(1)).findById(locationId);
        verify(bankRepository, times(1)).save(bank);
    }
}
