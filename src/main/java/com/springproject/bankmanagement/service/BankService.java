package com.springproject.bankmanagement.service;

import com.springproject.bankmanagement.dao.CustomerRepository;
import com.springproject.bankmanagement.dao.BankRepository;
import com.springproject.bankmanagement.dao.LocationRepository;
import com.springproject.bankmanagement.entity.Customer;
import com.springproject.bankmanagement.entity.Bank;
import com.springproject.bankmanagement.entity.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BankService {

    private final BankRepository bankRepository;

    private final CustomerRepository customerRepository;

    private final LocationRepository locationRepository;

    @Autowired
    public BankService(BankRepository bankRepository,CustomerRepository customerRepository,LocationRepository locationRepository) {
        this.bankRepository = bankRepository;
        this.customerRepository = customerRepository;
        this.locationRepository = locationRepository;
    }

    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    public Optional<Bank> getBankById(Long id) {
        return bankRepository.findById(id);
    }

    public Bank saveBank(Bank bank) {
        return bankRepository.save(bank);
    }

    public void deleteBank(Long id) {
        bankRepository.deleteById(id);
    }

    public Bank assignCustomerToBank(Long bankId, Long customerId) {
        Set<Customer> customerSet = null;
        Bank bank = bankRepository.findById(bankId).get();
        Customer customer = customerRepository.findById(customerId).get();
        customerSet =  bank.getCustomers();
        customerSet.add(customer);
        bank.setCustomers(customerSet);
        return bankRepository.save(bank);
    }

    public Bank assignLocationToBank(Long bankId, Long locationId) {
        Set<Location> locationSet = null;
        Bank bank = bankRepository.findById(bankId).get();
        Location location = locationRepository.findById(locationId).get();
        locationSet =  bank.getLocations();
        locationSet.add(location);
        bank.setLocations(locationSet);
        return bankRepository.save(bank);
    }



}
