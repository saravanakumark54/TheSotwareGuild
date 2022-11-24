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
public class PowerDaoDBTest {
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
    
    public PowerDaoDBTest() {
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
    public void testAddGetPower() {
        Power power = new Power();
        power.setName("Test Name");
        power = powerDao.addPower(power);

        Power fromDao = powerDao.getPowerById(power.getId());
        assertEquals(power, fromDao);
    }

    @Test
    public void testGetAllPowers(){
        Power power = new Power();
        power.setName("Test Name");
        power = powerDao.addPower(power);

        Power power2 = new Power();
        power2.setName("Test Name 2");
        power2 = powerDao.addPower(power2);

        Power power3 = new Power();
        power3.setName("Test Name 3");
        power3 = powerDao.addPower(power3);

        List<Power> powers = powerDao.getAllPowers();

        assertEquals(3, powers.size());
        assertTrue(powers.contains(power));
        assertTrue(powers.contains(power2));
        assertTrue(powers.contains(power3));
    }

    @Test
    public void testUpdatePower() {
        Power power = new Power();
        power.setName("Test Name");
        power = powerDao.addPower(power);

        Power fromDao = powerDao.getPowerById(power.getId());
        assertEquals(power, fromDao);

        power.setName("New Test Name");
        powerDao.updatePower(power);

        assertNotEquals(power, fromDao);

        fromDao = powerDao.getPowerById(power.getId());
        assertEquals(power, fromDao);
    }

    @Test
    public void testDeletePower() {
        Power power = new Power();
        power.setName("Test Name");
        power = powerDao.addPower(power);

        Super superhero = new Super();
        superhero.setName("Test Name");
        superhero.setDescription("Test Description");
        superhero.setPower(power);
        superhero.setOrganizations(new ArrayList<Organization>());
        superhero.setImagePath("imagePath");
        superhero = superDao.addSuper(superhero);

        Power fromDao = powerDao.getPowerById(power.getId());
        assertEquals(power, fromDao);

        powerDao.deletePowerById(power.getId());

        fromDao = powerDao.getPowerById(power.getId());
        assertNull(fromDao);

    }
    
}
