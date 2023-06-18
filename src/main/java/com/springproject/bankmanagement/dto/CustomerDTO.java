package com.springproject.bankmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;


import com.springproject.bankmanagement.entity.Bank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long id;
    private String firstName;
    private String lastName;

    public CustomerDTO(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @JsonIgnore
    private Set<Bank> banks;


}
