package com.springproject.bankmanagement.dao;


import com.springproject.bankmanagement.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}