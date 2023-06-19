package com.springproject.bankmanagement.rest;

import com.springproject.bankmanagement.dto.LocationDTO;
import com.springproject.bankmanagement.entity.Location;
import com.springproject.bankmanagement.service.LocationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bank_mgmt/locations")
public class LocationController {

    private final LocationService LocationService;
    private final ModelMapper modelMapper;

    @Autowired
    public LocationController(LocationService LocationService, ModelMapper modelMapper) {
        this.LocationService = LocationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        List<Location> Locations = LocationService.getAllLocations();
        List<LocationDTO> LocationDTOs = Locations.stream()
                .map(this::entityToDTO)
                .toList();
        return new ResponseEntity<>(LocationDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        Optional<Location> Location = LocationService.getLocationById(id);
        return Location.map(value -> new ResponseEntity<>(entityToDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO LocationDTO) {
        Location Location = dtoToEntity(LocationDTO);
        Location createdLocation = LocationService.saveLocation(Location);
        LocationDTO createdLocationDTO = entityToDTO(createdLocation);
        return new ResponseEntity<>(createdLocationDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable Long id, @RequestBody LocationDTO LocationDTO) {
        Optional<Location> existingLocation = LocationService.getLocationById(id);
        if (existingLocation.isPresent()) {
            Location Location = dtoToEntity(LocationDTO);
            Location.setId(id);
            Location updatedLocation = LocationService.saveLocation(Location);
            LocationDTO updatedLocationDTO = entityToDTO(updatedLocation);
            return new ResponseEntity<>(updatedLocationDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        Optional<Location> existingLocation = LocationService.getLocationById(id);
        if (existingLocation.isPresent()) {
            LocationService.deleteLocation(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private Location dtoToEntity(LocationDTO LocationDTO) {
        return modelMapper.map(LocationDTO, Location.class);
    }

    private LocationDTO entityToDTO(Location Location) {
        return modelMapper.map(Location, LocationDTO.class);
    }
}
