/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightings.dao;

import com.sg.superherosightings.entities.Location;
import com.sg.superherosightings.entities.Organization;
import com.sg.superherosightings.entities.Power;
import com.sg.superherosightings.entities.Sighting;
import com.sg.superherosightings.entities.Super;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author K SARAVANA
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationDaoDBTest {
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    PowerDao powerDao;

    @Autowired
    SuperDao superDao;

    @Autowired
    OrganizationDao orgDao;
    
    @Autowired
    SightingDao sightingDao;
    
    public LocationDaoDBTest() {
    }
    
    
    @BeforeEach
    public void setUp() {
        List<Organization> orgs = orgDao.getAllOrgs();
        for (Organization org : orgs) {
            orgDao.deleteOrgById(org.getId());
        }

        List<Sighting> sightings = sightingDao.getAllSightings();
        for (Sighting sighting : sightings) {
            sightingDao.deleteSightingById(sighting.getId());
        }

        List<Super> supers = superDao.getAllSupers();
        for (Super hero : supers) {
            superDao.deleteSuperById(hero.getId());
        }

        List<Power> powers = powerDao.getAllPowers();
        for (Power power : powers) {
            powerDao.deletePowerById(power.getId());
        }

        List<Location> locations = locationDao.getAllLocations();
        for (Location location : locations) {
            locationDao.deleteLocationById(location.getId());
        }
    }

    @Test
    public void testAddGetLocation() {
        Location location = new Location();
        location.setName("Test Name");
        location.setDescription("Test Description");
        location.setAddress("Test Address");
        location.setLatitude("40.77");
        location.setLongitude("50.55");
        location = locationDao.addLocation(location);

        Location fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);
    }

    @Test
    public void testGetAllLocations() {
        Location location = new Location();
        location.setName("Test Name");
        location.setDescription("Test Description");
        location.setAddress("Test Address");
        location.setLatitude("40.77");
        location.setLongitude("50.55");
        location = locationDao.addLocation(location);

        Location location2 = new Location();
        location2.setName("Test Name2");
        location2.setDescription("Test Description2");
        location2.setAddress("Test Address2");
        location2.setLatitude("50.25");
        location2.setLongitude("50.00");
        location2 = locationDao.addLocation(location2);

        List<Location> locations = locationDao.getAllLocations();
        assertEquals(2, locations.size());
        assertTrue(locations.contains(location));
        assertTrue(locations.contains(location2));
    }

    @Test
    public void testUpdateLocation() {
        Location location = new Location();
        location.setName("Test Name");
        location.setDescription("Test Description");
        location.setAddress("Test Address");
        location.setLatitude("40.77");
        location.setLongitude("50.55");
        location = locationDao.addLocation(location);

        Location fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);

        location.setName(" New Test Name");
        locationDao.updateLocation(location);
        assertNotEquals(location, fromDao);

        fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);
    }

    @Test
    public void testDeleteLocation() {
        Power power = new Power();
        power.setName("Test Name");
        power = powerDao.addPower(power);

        Super hero = new Super();
        hero.setName("Test Name");
        hero.setDescription("Test Description");
        hero.setPower(power);
        hero.setOrganizations(new ArrayList<Organization>());
        hero.setImagePath("imagePath");
        hero = superDao.addSuper(hero);

        Location location = new Location();
        location.setName("Test Name");
        location.setDescription("Test Description");
        location.setAddress("Test Address");
        location.setLatitude("40.77");
        location.setLongitude("50.55");
        location = locationDao.addLocation(location);

        Sighting sighting = new Sighting();
        sighting.setSuperhero(hero);
        sighting.setLocation(location);
        sighting.setDate(LocalDate.now());
        sighting = sightingDao.addSighting(sighting);

        locationDao.deleteLocationById(location.getId());

        Sighting sightingFromDao = sightingDao.getSightingById(sighting.getId());
        assertNull(sightingFromDao);

        Location locationFromDao = locationDao.getLocationById(location.getId());
        assertNull(locationFromDao);
    }

    @Test
    public void testGetLocationBySuper() {
        Power power = new Power();
        power.setName("Test Name");
        power = powerDao.addPower(power);

        Super hero = new Super();
        hero.setName("Test Name");
        hero.setDescription("Test Description");
        hero.setPower(power);
        hero.setOrganizations(new ArrayList<Organization>());
        hero.setImagePath("imagePath");
        hero = superDao.addSuper(hero);

        Location location = new Location();
        location.setName("Test Name");
        location.setDescription("Test Description");
        location.setAddress("Test Address");
        location.setLatitude("40.77");
        location.setLongitude("50.55");
        location = locationDao.addLocation(location);

        Sighting sighting = new Sighting();
        sighting.setSuperhero(hero);
        sighting.setLocation(location);
        sighting.setDate(LocalDate.now());
        sighting = sightingDao.addSighting(sighting);

        List<Location> locations = locationDao.getLocationsBySuper(hero);
        assertTrue(locations.contains(location));
    }
}
