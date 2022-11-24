/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightings.controller;

import com.sg.superherosightings.entities.Location;
import com.sg.superherosightings.entities.Sighting;
import com.sg.superherosightings.entities.Super;
import com.sg.superherosightings.service.SuperService;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author K SARAVANA
 */
@Controller
public class SightingController {
    @Autowired
    SuperService service;

    Set<ConstraintViolation<Sighting>> violations = new HashSet<>();

    @GetMapping("/")
    public String displayLastTenSightings(Model model) {
        List<Sighting> lastTenSightings = service.getLastTenSightings();
        model.addAttribute("lastTenSightings", lastTenSightings);
        return "index";
    }

    @GetMapping("sightings")
    public String displaySightings(Model model) {
        List<Sighting> sightings = service.getAllSightings();
        List<Super> supers = service.getAllSupers();
        List<Location> locations = service.getAllLocations();
        LocalDate now = LocalDate.now();
        model.addAttribute("sightings", sightings);
        model.addAttribute("supers", supers);
        model.addAttribute("locations", locations);
        model.addAttribute("now", now);
        model.addAttribute("errors", violations);
        return "sightings";
    }

    @GetMapping("sightingsByDate")
    public String displaySightingsByDate(String date, Model model) {
        LocalDate sightingDate = LocalDate.parse(date);
        List<Sighting> sightings = service.getSightingsByDate(sightingDate);
        List<Super> supers = service.getAllSupers();
        List<Location> locations = service.getAllLocations();
        LocalDate now = LocalDate.now();
        model.addAttribute("sightings", sightings);
        model.addAttribute("supers", supers);
        model.addAttribute("locations", locations);
        model.addAttribute("now", now);
        model.addAttribute("errors", violations);
        return "sightings";
    }

    @PostMapping("addSighting")
    public String addSighting(HttpServletRequest request) {
        String superId = request.getParameter("superId");
        String locationId = request.getParameter("locationId");
        String date = request.getParameter("date");
        Super hero = service.getSuperById(Integer.parseInt(superId));
        Location location = service.getLocationById(Integer.parseInt(locationId));
        LocalDate sightingDate = LocalDate.parse(date);

        Sighting sighting = new Sighting();
        sighting.setSuperhero(hero);
        sighting.setLocation(location);
        sighting.setDate(sightingDate);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(sighting);

        if (violations.isEmpty()) {
            service.addSighting(sighting);
        }

        return "redirect:/sightings";
    }

    @GetMapping("deleteSighting")
    public String deleteSighting(Integer id, Model model) {
        service.deleteSightingById(id);
        return "redirect:/sightings";
    }

    @GetMapping("editSighting")
    public String editSighting(Integer id, Model model) {
        Sighting sighting = service.getSightingById(id);
        List<Super> supers = service.getAllSupers();
        List<Location> locations = service.getAllLocations();
        LocalDate now = LocalDate.now();
        model.addAttribute("sighting", sighting);
        model.addAttribute("supers", supers);
        model.addAttribute("locations", locations);
        model.addAttribute("now", now);
        return "editSighting";
    }

    @PostMapping("editSighting")
    public String performEditSighting(String id, String superId, String locationId, String date, HttpServletRequest request) {
        Super hero = service.getSuperById(Integer.parseInt(superId));
        Location location = service.getLocationById(Integer.parseInt(locationId));
        LocalDate sightingDate = LocalDate.parse(date);

        Sighting sighting = new Sighting();
        sighting.setId(Integer.parseInt(id));
        sighting.setSuperhero(hero);
        sighting.setLocation(location);
        sighting.setDate(sightingDate);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(sighting);

        if (violations.isEmpty()) {
            service.updateSighting(sighting);
        }

        return "redirect:/sightings";
    }
}
