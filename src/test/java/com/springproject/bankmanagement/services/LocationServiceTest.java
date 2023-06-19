package com.springproject.bankmanagement.services;

import com.springproject.bankmanagement.dao.LocationRepository;
import com.springproject.bankmanagement.entity.Location;
import com.springproject.bankmanagement.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLocations() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location(1L,  "Location 1", new HashSet<>()));
        locations.add(new Location(2L, "Location 2", new HashSet<>()));
        when(locationRepository.findAll()).thenReturn(locations);

        List<Location> result = locationService.getAllLocations();

        assertEquals(2, result.size());
        assertEquals(locations, result);
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    void testGetLocationById() {
        Location location = new Location(1L,  "Location 1", new HashSet<>());
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));

        Optional<Location> result = locationService.getLocationById(1L);

        assertTrue(result.isPresent());
        assertEquals(location, result.get());
        verify(locationRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveLocation() {
        Location location = new Location(1L,  "Location 1", new HashSet<>());
        when(locationRepository.save(location)).thenReturn(location);

        Location result = locationService.saveLocation(location);

        assertEquals(location, result);
        verify(locationRepository, times(1)).save(location);
    }

    @Test
    void testDeleteLocation() {
        Long locationId = 1L;

        locationService.deleteLocation(locationId);

        verify(locationRepository, times(1)).deleteById(locationId);
    }
}
