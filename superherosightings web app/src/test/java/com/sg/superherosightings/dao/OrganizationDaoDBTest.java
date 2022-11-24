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
import static org.junit.jupiter.api.Assertions.assertFalse;
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
public class OrganizationDaoDBTest {
    
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
    
    public OrganizationDaoDBTest() {
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
    public void testAddGetOrg() {
        Organization org = new Organization();
        org.setName("Test Name");
        org.setDescription("Test Description");
        org.setAddress("Test Address");
        org.setContact("Test Contact");
        org.setSupers(new ArrayList<Super>());
        org = orgDao.addOrg(org);

        Organization fromDao = orgDao.getOrgById(org.getId());
        assertEquals(org, fromDao);
    }

    @Test
    public void testGetAllOrgs() {
        Organization org = new Organization();
        org.setName("Test Name");
        org.setDescription("Test Description");
        org.setAddress("Test Address");
        org.setContact("Test Contact");
        org.setSupers(new ArrayList<Super>());
        org = orgDao.addOrg(org);

        Organization org2 = new Organization();
        org2.setName("Test Name 2");
        org2.setDescription("Test Description 2");
        org2.setAddress("Test Address 2");
        org2.setContact("Test Contact 2");
        org2.setSupers(new ArrayList<Super>());
        org2 = orgDao.addOrg(org2);

        List<Organization> orgs = orgDao.getAllOrgs();
        assertEquals(2, orgs.size());
        assertTrue(orgs.contains(org));
        assertTrue(orgs.contains(org2));
    }

    @Test
    public void testUpdateOrg() {
        Organization org = new Organization();
        org.setName("Test Name");
        org.setDescription("Test Description");
        org.setAddress("Test Address");
        org.setContact("Test Contact");
        org.setSupers(new ArrayList<Super>());
        org = orgDao.addOrg(org);

        Organization fromDao = orgDao.getOrgById(org.getId());
        assertEquals(org, fromDao);

        org.setDescription("New Test Description");
        orgDao.updateOrg(org);
        assertNotEquals(org, fromDao);

        fromDao = orgDao.getOrgById(org.getId());
        assertEquals(org, fromDao);
    }

    @Test
    public void testDeleteOrg() {
        Organization org = new Organization();
        org.setName("Test Name");
        org.setDescription("Test Description");
        org.setAddress("Test Address");
        org.setContact("Test Contact");
        org.setSupers(new ArrayList<Super>());
        org = orgDao.addOrg(org);

        Organization fromDao = orgDao.getOrgById(org.getId());
        assertEquals(org, fromDao);

        orgDao.deleteOrgById(org.getId());
        fromDao = orgDao.getOrgById(org.getId());
        assertNull(fromDao);
    }

    @Test
    public void testGetOrgBySuper() {
        Organization org = new Organization();
        org.setName("Test Name");
        org.setDescription("Test Description");
        org.setAddress("Test Address");
        org.setContact("Test Contact");
        org.setSupers(new ArrayList<Super>());
        org = orgDao.addOrg(org);

        Power power = new Power();
        power.setName("Test Name");
        power = powerDao.addPower(power);

        Super hero = new Super();
        hero.setName("Test Name");
        hero.setDescription("Test Description");
        hero.setPower(power);
        hero.setOrganizations(new ArrayList<Organization>());
        hero = superDao.addSuper(hero);

        hero.getOrganizations().add(org);
        superDao.updateSuper(hero);

        org.getSupers().add(hero);
        orgDao.updateOrg(org);

        List<Organization> orgs = orgDao.getOrgBySuper(hero);
        assertEquals(1, orgs.size());
        assertFalse(orgs.contains(org));

    }
}
