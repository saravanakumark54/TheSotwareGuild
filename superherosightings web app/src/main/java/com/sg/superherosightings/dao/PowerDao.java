/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightings.dao;

import com.sg.superherosightings.entities.Power;
import com.sg.superherosightings.entities.Super;
import java.util.List;

/**
 *
 * @author K SARAVANA
 */
public interface PowerDao {
    
    Power getPowerById(int id);

    List<Power> getAllPowers();

    Power addPower(Power power);

    void updatePower(Power power);

    void deletePowerById(int id);

    Power getPowerBySuper(Super superhero);
}
