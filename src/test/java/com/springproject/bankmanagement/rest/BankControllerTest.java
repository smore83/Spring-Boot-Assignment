package com.springproject.bankmanagement.rest;

import com.springproject.bankmanagement.dto.BankDTO;
import com.springproject.bankmanagement.entity.Bank;
import com.springproject.bankmanagement.service.BankService;
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

class BankControllerTest {

    @Mock
    private BankService bankService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BankController bankController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllBanks_ReturnsListOfBankDTOs() {
        List<Bank> banks = Arrays.asList(
                new Bank(1L, "Bank 1", "Customer 1"),
                new Bank(2L, "Bank 2", "Customer 2")
        );

        when(bankService.getAllBanks()).thenReturn(banks);
        when(modelMapper.map(any(Bank.class), eq(BankDTO.class))).thenReturn(new BankDTO());

        ResponseEntity<List<BankDTO>> response = bankController.getAllBanks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getBankById_WithValidId_ReturnsBankDTO() {
        long bankId = 1L;
        Bank bank = new Bank(bankId, "Bank 1", "Customer 1");
        BankDTO bankDTO = new BankDTO(bankId, "Bank 1", "Customer 1");
        when(bankService.getBankById(bankId)).thenReturn(Optional.of(bank));
        when(modelMapper.map(bank, BankDTO.class)).thenReturn(bankDTO);

        ResponseEntity<BankDTO> response = bankController.getBankById(bankId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bankDTO, response.getBody());
    }

    @Test
    void getBankById_WithInvalidId_ReturnsNotFound() {
        Long bankId = 1L;
        when(bankService.getBankById(bankId)).thenReturn(Optional.empty());

        ResponseEntity<BankDTO> response = bankController.getBankById(bankId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createBank_ReturnsCreatedBankDTO() {
        BankDTO bankDTO = new BankDTO(1L, "Bank 1", "Customer 1");
        Bank bank = new Bank(1L, "Bank 1", "Customer 1");
        when(modelMapper.map(bankDTO, Bank.class)).thenReturn(bank);
        when(bankService.saveBank(bank)).thenReturn(bank);
        when(modelMapper.map(bank, BankDTO.class)).thenReturn(bankDTO);

        ResponseEntity<BankDTO> response = bankController.createBank(bankDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bankDTO, response.getBody());
    }

    @Test
    void updateBank_WithExistingBank_ReturnsUpdatedBankDTO() {
        long bankId = 1L;
        BankDTO updatedBankDTO = new BankDTO(bankId, "Updated Bank", "Updated Customer");
        Bank updatedBank = new Bank(bankId, "Updated Bank", "Updated Customer");
        when(modelMapper.map(updatedBankDTO, Bank.class)).thenReturn(updatedBank);
        when(bankService.getBankById(bankId)).thenReturn(Optional.of(new Bank()));
        when(bankService.saveBank(updatedBank)).thenReturn(updatedBank);
        when(modelMapper.map(updatedBank, BankDTO.class)).thenReturn(updatedBankDTO);

        ResponseEntity<BankDTO> response = bankController.updateBank(bankId, updatedBankDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBankDTO, response.getBody());
    }

    @Test
    void updateBank_WithNonexistentBank_ReturnsNotFound() {
        Long bankId = 1L;
        BankDTO updatedBankDTO = new BankDTO(bankId, "Updated Bank", "Updated Customer");
        when(bankService.getBankById(bankId)).thenReturn(Optional.empty());

        ResponseEntity<BankDTO> response = bankController.updateBank(bankId, updatedBankDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteBank_WithExistingBank_ReturnsNoContent() {
        Long bankId = 1L;
        when(bankService.getBankById(bankId)).thenReturn(Optional.of(new Bank()));

        ResponseEntity<Void> response = bankController.deleteBank(bankId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bankService, times(1)).deleteBank(bankId);
    }

    @Test
    void deleteBank_WithNonexistentBank_ReturnsNotFound() {
        Long bankId = 1L;
        when(bankService.getBankById(bankId)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = bankController.deleteBank(bankId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bankService, never()).deleteBank(bankId);
    }
}
