package com.springproject.bankmanagement.controller;

import com.springproject.bankmanagement.dto.LocationDTO;
import com.springproject.bankmanagement.entity.Location;
import com.springproject.bankmanagement.service.LocationService;
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

class LocationControllerTest {

    @Mock
    private LocationService locationService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private LocationController locationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllLocations_ReturnsListOfLocationDTOs() {
        List<Location> locations = Arrays.asList(
                new Location(1L, "Location 1"),
                new Location(2L, "Location 2")
        );

        when(locationService.getAllLocations()).thenReturn(locations);
        when(modelMapper.map(any(Location.class), eq(LocationDTO.class))).thenReturn(new LocationDTO());

        ResponseEntity<List<LocationDTO>> response = locationController.getAllLocations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getLocationById_WithValidId_ReturnsLocationDTO() {
        long locationId = 1L;
        Location location = new Location(locationId, "Location 1");
        LocationDTO locationDTO = new LocationDTO(locationId, "Location 1");
        when(locationService.getLocationById(locationId)).thenReturn(Optional.of(location));
        when(modelMapper.map(location, LocationDTO.class)).thenReturn(locationDTO);

        ResponseEntity<LocationDTO> response = locationController.getLocationById(locationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locationDTO, response.getBody());
    }

    @Test
    void getLocationById_WithInvalidId_ReturnsNotFound() {
        Long locationId = 1L;
        when(locationService.getLocationById(locationId)).thenReturn(Optional.empty());

        ResponseEntity<LocationDTO> response = locationController.getLocationById(locationId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createLocation_ReturnsCreatedLocationDTO() {
        LocationDTO locationDTO = new LocationDTO(1L, "Location 1");
        Location location = new Location(1L, "Location 1");
        when(modelMapper.map(locationDTO, Location.class)).thenReturn(location);
        when(locationService.saveLocation(location)).thenReturn(location);
        when(modelMapper.map(location, LocationDTO.class)).thenReturn(locationDTO);

        ResponseEntity<LocationDTO> response = locationController.createLocation(locationDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(locationDTO, response.getBody());
    }

    @Test
    void updateLocation_WithExistingLocation_ReturnsUpdatedLocationDTO() {
        long locationId = 1L;
        LocationDTO updatedLocationDTO = new LocationDTO(locationId, "Updated Location");
        Location updatedLocation = new Location(locationId, "Updated Location");
        when(modelMapper.map(updatedLocationDTO, Location.class)).thenReturn(updatedLocation);
        when(locationService.getLocationById(locationId)).thenReturn(Optional.of(new Location()));
        when(locationService.saveLocation(updatedLocation)).thenReturn(updatedLocation);
        when(modelMapper.map(updatedLocation, LocationDTO.class)).thenReturn(updatedLocationDTO);

        ResponseEntity<LocationDTO> response = locationController.updateLocation(locationId, updatedLocationDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedLocationDTO, response.getBody());
    }

    @Test
    void updateLocation_WithNonexistentLocation_ReturnsNotFound() {
        Long locationId = 1L;
        LocationDTO updatedLocationDTO = new LocationDTO(locationId, "Updated Location");
        when(locationService.getLocationById(locationId)).thenReturn(Optional.empty());

        ResponseEntity<LocationDTO> response = locationController.updateLocation(locationId, updatedLocationDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteLocation_WithExistingLocation_ReturnsNoContent() {
        Long locationId = 1L;
        when(locationService.getLocationById(locationId)).thenReturn(Optional.of(new Location()));

        ResponseEntity<Void> response = locationController.deleteLocation(locationId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(locationService, times(1)).deleteLocation(locationId);
    }

    @Test
    void deleteLocation_WithNonexistentLocation_ReturnsNotFound() {
        Long locationId = 1L;
        when(locationService.getLocationById(locationId)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = locationController.deleteLocation(locationId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(locationService, never()).deleteLocation(locationId);
    }


}
