package com.springproject.bankmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

//    @Column(nullable = false)
//    private String branchname;

    @Column(nullable = false)
    private String location;

    @JsonIgnore
    @ManyToMany(mappedBy = "locations")
    private Set<Bank> banks = new HashSet<>();

    public Location(long l, String s) {
    }
}

