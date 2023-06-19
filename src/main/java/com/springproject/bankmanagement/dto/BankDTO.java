package com.springproject.bankmanagement.dto;


import com.springproject.bankmanagement.entity.Customer;
import com.springproject.bankmanagement.entity.Location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankDTO {
    private Long id;
    private String title;
    private String ifscno;
    private Set<Customer> customers;
    private Set<Location> locations;

    public BankDTO(long l, String s, String s1) {
    }
}
