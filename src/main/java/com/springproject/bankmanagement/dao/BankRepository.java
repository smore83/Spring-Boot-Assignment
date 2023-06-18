package com.springproject.bankmanagement.dao;


import com.springproject.bankmanagement.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {
}