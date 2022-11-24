/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightings.dao;

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
public class PowerDaoDB implements PowerDao{
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Power getPowerById(int id) {
        try {
            final String GET_POWER_BY_ID = "SELECT * FROM Power WHERE id = ?";
            return jdbc.queryForObject(GET_POWER_BY_ID, new PowerMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Power> getAllPowers() {
        final String GET_ALL_POWERS = "SELECT * FROM Power";
        return jdbc.query(GET_ALL_POWERS, new PowerMapper());
    }

    @Override
    @Transactional
    public Power addPower(Power power) {
        final String INSERT_POWER = "INSERT INTO Power(name) VALUES (?)";
        jdbc.update(INSERT_POWER, power.getName());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        power.setId(newId);
        return power;
    }

    @Override
    @Transactional
    public void updatePower(Power power) {
        final String UPDATE_POWER = "UPDATE Power SET name = ? WHERE id = ?";
        jdbc.update(UPDATE_POWER, power.getName(), power.getId());
    }

    @Override
    @Transactional
    public void deletePowerById(int id) {
        final String DELETE_SUPERPOWERS = "DELETE FROM Super WHERE powerId = ?";
        jdbc.update(DELETE_SUPERPOWERS, id);

        final String DELETE_POWER = "DELETE FROM Power WHERE id = ?";
        jdbc.update(DELETE_POWER, id);
    }

    @Override
    public Power getPowerBySuper(Super superhero) {
        try {
            final String GET_POWER_BY_SUPER = "SELECT * FROM Power WHERE id = ?";
            return jdbc.queryForObject(GET_POWER_BY_SUPER, new PowerMapper(), superhero.getPower().getId());
        } catch (DataAccessException ex) {
            return null;
        }
    }

    public static final class PowerMapper implements RowMapper<Power> {

        @Override
        public Power mapRow(ResultSet rs, int index) throws SQLException {
            Power power = new Power();
            power.setId(rs.getInt("id"));
            power.setName(rs.getString("name"));
            return power;
        }

    }

}
