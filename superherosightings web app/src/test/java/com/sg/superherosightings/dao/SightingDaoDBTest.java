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
import java.time.Month;
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
public class SightingDaoDBTest {
    @Autowired
    PowerDao powerDao;

    @Autowired
    SuperDao superDao;

    @Autowired
    OrganizationDao orgDao;

    @Autowired
    LocationDao locationDao;

    @Autowired
    SightingDao sightingDao;

    public SightingDaoDBTest() {
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
    public void testAddGetSighting() {
        Power power = new Power();
        power.setName("Test Name");
        power = powerDao.addPower(power);

        Super hero = new Super();
        hero.setName("Test Name");
        hero.setDescription("Test Description");
        hero.setPower(power);
        hero.setOrganizations(new ArrayList<Organization>());
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
        sighting.setDate(LocalDate.of(2022, Month.JUNE, 22));
        sighting = sightingDao.addSighting(sighting);

        Sighting fromDao = sightingDao.getSightingById(sighting.getId());
        assertEquals(sighting, fromDao);
    }

    @Test
    public void testGetAllSightings() {
        Power power = new Power();
        power.setName("Test Name");
        power = powerDao.addPower(power);

        Super hero = new Super();
        hero.setName("Test Name");
        hero.setDescription("Test Description");
        hero.setPower(power);
        hero.setOrganizations(new ArrayList<Organization>());
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
        sighting.setDate(LocalDate.of(2022, Month.JUNE, 22));
        sighting = sightingDao.addSighting(sighting);

        Sighting sighting2 = new Sighting();
        sighting2.setSuperhero(hero);
        sighting2.setLocation(location);
        sighting2.setDate(LocalDate.of(2022, Month.JUNE, 23));
        sighting2 = sightingDao.addSighting(sighting2);

        List<Sighting> sightings = sightingDao.getAllSightings();
        assertEquals(2, sightings.size());
        assertTrue(sightings.contains(sighting));
        assertTrue(sightings.contains(sighting2));
    }

    @Test
    public void testUpdateSightings() {
        Power power = new Power();
        power.setName("Test Name");
        power = powerDao.addPower(power);

        Super hero = new Super();
        hero.setName("Test Name");
        hero.setDescription("Test Description");
        hero.setPower(power);
        hero.setOrganizations(new ArrayList<Organization>());
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
        sighting.setDate(LocalDate.of(2022, Month.JUNE, 22));
        sighting = sightingDao.addSighting(sighting);

        Sighting fromDao = sightingDao.getSightingById(sighting.getId());
        assertEquals(sighting, fromDao);

        sighting.setDate(LocalDate.of(2022, Month.JUNE, 24));
        sightingDao.updateSighting(sighting);
        assertNotEquals(sighting, fromDao);

        fromDao = sightingDao.getSightingById(sighting.getId());
        assertEquals(sighting, fromDao);
    }

    @Test
    public void testDeleteSighting() {
        Power power = new Power();
        power.setName("Test Name");
        power = powerDao.addPower(power);

        Super hero = new Super();
        hero.setName("Test Name");
        hero.setDescription("Test Description");
        hero.setPower(power);
        hero.setOrganizations(new ArrayList<Organization>());
        hero = superDao.addSuper(hero);

        Location location = new Location();
        location.setName("Test Name");
        location.setDescription("Test Description");
        location.setAddress("test Address");
        location.setLatitude("40.77");
        location.setLongitude("50.55");
        location = locationDao.addLocation(location);

        Sighting sighting = new Sighting();
        sighting.setSuperhero(hero);
        sighting.setLocation(location);
        sighting.setDate(LocalDate.of(2022, Month.JUNE, 22));
        sighting = sightingDao.addSighting(sighting);

        sightingDao.deleteSightingById(sighting.getId());

        Sighting fromDao = sightingDao.getSightingById(sighting.getId());
        assertNull(fromDao);
    }

    @Test
    public void testGetSightingByDate() {
        Power power = new Power();
        power.setName("Test Name");
        power = powerDao.addPower(power);

        Super hero = new Super();
        hero.setName("Test Name");
        hero.setDescription("Test Description");
        hero.setPower(power);
        hero.setOrganizations(new ArrayList<Organization>());
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
        sighting.setDate(LocalDate.of(2022, Month.JUNE, 22));
        sighting = sightingDao.addSighting(sighting);

        List<Sighting> sightings = sightingDao.getSightingsByDate(LocalDate.of(2022, Month.JUNE, 22));

        assertEquals(1, sightings.size());
        assertTrue(sightings.contains(sighting));

    }

}
