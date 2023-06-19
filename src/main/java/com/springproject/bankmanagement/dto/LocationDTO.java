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
public class LocationDTO {
    private Long id;
    private String address;
    @JsonIgnore
    private Set<Bank> banks;

    public LocationDTO(long l, String s) {
    }
}
