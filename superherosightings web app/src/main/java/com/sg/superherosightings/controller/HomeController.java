/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightings.controller;

import com.sg.superherosightings.entities.Sighting;
import com.sg.superherosightings.service.SuperService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author K SARAVANA
 */
@Controller
public class HomeController {
    @Autowired
    SuperService service;
    
    @GetMapping("index")
    public String displayLastTenSightings(Model model) {
        List<Sighting> lastTenSightings = service.getLastTenSightings();
        model.addAttribute("lastTenSightings", lastTenSightings);
        return "index";
    }
}
