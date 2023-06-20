package com.springproject.bankmanagement.rest;

import com.springproject.bankmanagement.dto.BankDTO;
import com.springproject.bankmanagement.entity.Bank;

import com.springproject.bankmanagement.entity.Location;
import com.springproject.bankmanagement.service.BankService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bank_mgmt/banks")
public class BankController {

    private final BankService bankService;
    private final ModelMapper modelMapper;

    @Autowired
    public BankController(BankService bankService, ModelMapper modelMapper) {
        this.bankService = bankService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<BankDTO>> getAllBanks() {
        List<Bank> banks = bankService.getAllBanks();
        List<BankDTO> bankDTOs = banks.stream()
                .map(this::entityToDTO)
                .toList();
        return new ResponseEntity<>(bankDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankDTO> getBankById(@PathVariable Long id) {
        Optional<Bank> bank = bankService.getBankById(id);
        return bank.map(value -> new ResponseEntity<>(entityToDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<BankDTO> createBank(@RequestBody BankDTO bankDTO) {
        Bank bank = dtoToEntity(bankDTO);
        Bank createdBank = bankService.saveBank(bank);
        BankDTO createdBankDTO = entityToDTO(createdBank);
        return new ResponseEntity<>(createdBankDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankDTO> updateBank(@PathVariable Long id, @RequestBody BankDTO bankDTO) {
        Optional<Bank> existingBank = bankService.getBankById(id);
        if (existingBank.isPresent()) {
            Bank bank = dtoToEntity(bankDTO);
            bank.setId(id);
            Bank updatedBank = bankService.saveBank(bank);
            BankDTO updatedBankDTO = entityToDTO(updatedBank);
            return new ResponseEntity<>(updatedBankDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{bankId}/customer/{customerId}")
    public Bank assignCustomerToBank(
            @PathVariable Long bankId,
            @PathVariable Long customerId
    ) {
        return bankService.assignCustomerToBank(bankId, customerId);
    }

    @PutMapping("/{bankId}/location/{locationId}")
    public Bank assignLocationToBank(
            @PathVariable Long bankId,
            @PathVariable Long locationId
    ) {
        return bankService.assignLocationToBank(bankId, locationId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBank(@PathVariable Long id) {
        Optional<Bank> existingBank = bankService.getBankById(id);
        if (existingBank.isPresent()) {
            bankService.deleteBank(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private Bank dtoToEntity(BankDTO bankDTO) {
        return modelMapper.map(bankDTO, Bank.class);
    }

    private BankDTO entityToDTO(Bank bank) {
        return modelMapper.map(bank, BankDTO.class);
    }
}
