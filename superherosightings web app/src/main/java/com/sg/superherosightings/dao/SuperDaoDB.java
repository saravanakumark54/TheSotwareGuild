/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightings.dao;

import com.sg.superherosightings.dao.OrganizationDaoDB.OrganizationMapper;
import com.sg.superherosightings.dao.PowerDaoDB.PowerMapper;
import com.sg.superherosightings.entities.Location;
import com.sg.superherosightings.entities.Organization;
import com.sg.superherosightings.entities.Power;
import com.sg.superherosightings.entities.Super;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author K SARAVANA
 */
@Repository
public class SuperDaoDB implements SuperDao{
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Super getSuperById(int id) {
        try {
            final String GET_SUPER_BY_ID = "SELECT * FROM Super WHERE id = ?";
            Super superhero = jdbc.queryForObject(GET_SUPER_BY_ID, new SuperMapper(), id);
            superhero.setPower(getPowerForSuper(id));
            superhero.setOrganizations(getOrganizationsForSuper(id));
            return superhero;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Super> getAllSupers() {
        final String GET_ALL_SUPERS = "SELECT * FROM Super";
        List<Super> supers = jdbc.query(GET_ALL_SUPERS, new SuperMapper());
        for (Super superhero : supers) {
            superhero.setPower(getPowerForSuper(superhero.getId()));
            superhero.setOrganizations(getOrganizationsForSuper(superhero.getId()));
        }
        return supers;
    }

    @Override
    @Transactional
    public Super addSuper(Super superhero) {
        final String ADD_SUPER = "INSERT INTO Super(name, description, powerId, imagePath) VALUES(?,?,?,?)";
        if (superhero.getPower() != null) {
            jdbc.update(ADD_SUPER,
                    superhero.getName(),
                    superhero.getDescription(),
                    superhero.getPower().getId(),
                    superhero.getImagePath());
        } else {
            jdbc.update(ADD_SUPER,
                    superhero.getName(),
                    superhero.getDescription(),
                    null);
        }

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        superhero.setId(newId);
        insertOrganizationMember(superhero);
        return superhero;
    }

    @Override
    @Transactional
    public void updateSuper(Super superhero) {
        final String UPDATE_SUPER = "UPDATE Super SET name = ?, description = ?, powerId = ?, imagePath = ? WHERE id = ?";
        if (superhero.getPower() != null) {
            jdbc.update(UPDATE_SUPER,
                    superhero.getName(),
                    superhero.getDescription(),
                    superhero.getPower().getId(),
                    superhero.getImagePath(),
                    superhero.getId());
        } else {
            jdbc.update(UPDATE_SUPER,
                    superhero.getName(),
                    superhero.getDescription(),
                    null,
                    superhero.getImagePath(),
                    superhero.getId());
        }
        final String DELETE_ORGANIZATION_MEMBER = "DELETE FROM OrganizationMember WHERE superId = ?";
        jdbc.update(DELETE_ORGANIZATION_MEMBER, superhero.getId());
        insertOrganizationMember(superhero);
    }

    @Override
    @Transactional
    public void deleteSuperById(int id) {
        final String DELETE_SIGHTINGS = "DELETE FROM Sighting WHERE superId = ?";
        jdbc.update(DELETE_SIGHTINGS, id);

        final String DELETE_ORGANIZATION_MEMBER = "DELETE FROM OrganizationMember WHERE superId = ?";
        jdbc.update(DELETE_ORGANIZATION_MEMBER, id);

        final String DELETE_SUPER = "DELETE FROM Super WHERE id = ?";
        jdbc.update(DELETE_SUPER, id);
    }

    @Override
    public List<Super> getSupersByLocation(Location location) {
        final String GET_SUPERS_BY_LOCATION = "SELECT DISTINCT s.id, s.name, s.description, s.powerId, s.imagePath "
                + "FROM Sighting st "
                + "JOIN Super s ON st.superId = s.id "
                + "WHERE st.locationId = ?";
        List<Super> supers = jdbc.query(GET_SUPERS_BY_LOCATION, new SuperMapper(), location.getId());
        for (Super superhero : supers) {
            superhero.setPower(getPowerForSuper(superhero.getId()));
            superhero.setOrganizations(getOrganizationsForSuper(superhero.getId()));
        }
        return supers;
    }

    @Override
    public List<Super> getSupersByOrganization(Organization organization) {
        final String GET_SUPERS_BY_ORG = "SELECT s.id, s.name, s.description, s.powerId, s.imagePath "
                + "FROM OrganizationMember om"
                + "JOIN Super s ON om.superId = s.id "
                + "WHERE om.organizationId = ?";
        List<Super> supers = jdbc.query(GET_SUPERS_BY_ORG, new SuperMapper(), organization.getId());
        for (Super superhero : supers) {
            superhero.setPower(getPowerForSuper(superhero.getId()));
            superhero.setOrganizations(getOrganizationsForSuper(superhero.getId()));
        }
        return supers;
    }

    private Power getPowerForSuper(int id) {
        try {
            final String GET_POWER_FOR_SUPER = "SELECT p.id, p.name FROM Power p "
                    + "JOIN Super s ON p.id = s.powerId WHERE s.id = ?";
            return jdbc.queryForObject(GET_POWER_FOR_SUPER , new PowerMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    private List<Organization> getOrganizationsForSuper(int id) {
        final String GET_ORGANIZATION_FOR_SUPER = "SELECT o.id, o.name, o.description, o.address, o.contact "
                + "FROM OrganizationMember om "
                + "JOIN Organization o ON om.organizationId = o.id "
                + "WHERE om.superId = ?";
        List<Organization> organizations = jdbc.query(GET_ORGANIZATION_FOR_SUPER, new OrganizationMapper(), id);
        for (Organization organization : organizations) {
            organization.setSupers(getSupersForOrganization(organization));
        }
        return organizations;
    }

    private List<Super> getSupersForOrganization(Organization organization) {
        final String GET_SUPERS = "SELECT s.id, s.name, s.description, s.powerId, s.imagePath "
                + "FROM OrganizationMember om "
                + "JOIN Super s ON om.superId = s.id "
                + "WHERE om.organizationId = ?";
        List<Super> supers = jdbc.query(GET_SUPERS, new SuperMapper(), organization.getId());
        for (Super superhero : supers) {
            superhero.setPower(getPowerForSuper(superhero.getId()));
        }
        return supers;
    }

    private void insertOrganizationMember(Super superhero) {
        final String INSERT_ORGANIZATION_MEMBER = "INSERT INTO OrganizationMember VALUES(?,?)";
        for (Organization organization : superhero.getOrganizations()) {
            jdbc.update(INSERT_ORGANIZATION_MEMBER, organization.getId(), superhero.getId());
        }
    }

    @Override
    public void addImage(String imagePath, int id) {
        final String addImagePath = "Update Super Set imagePath = ? Where id = ?";
        jdbc.update(addImagePath, imagePath,id);
    }

    public final static class SuperMapper implements RowMapper<Super> {

        @Override
        public Super mapRow(ResultSet rs, int i) throws SQLException {
            Super superhero = new Super();
            superhero.setId(rs.getInt("id"));
            superhero.setName(rs.getString("name"));
            superhero.setDescription(rs.getString("description"));
            superhero.setImagePath(rs.getString("imagePath"));
            return superhero;
        }

    }

}
