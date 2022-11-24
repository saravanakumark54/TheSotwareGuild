/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightings.controller;

import com.sg.superherosightings.entities.Location;
import com.sg.superherosightings.entities.Organization;
import com.sg.superherosightings.entities.Power;
import com.sg.superherosightings.entities.Super;
import com.sg.superherosightings.service.SuperService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author K SARAVANA
 */
@Controller
public class SuperController {
    private static String UPLOADED_FOLDER = "src/main/resources/static/images/";
    @Autowired
    SuperService service;

    Set<ConstraintViolation<Super>> violations = new HashSet<>();

    @GetMapping("supers")
    public String displaySupers(Model model) {
        List<Super> supers = service.getAllSupers();
        List<Power> powers = service.getAllPowers();
        List<Organization> orgs = service.getAllOrgs();
        model.addAttribute("supers", supers);
        model.addAttribute("powers", powers);
        model.addAttribute("orgs", orgs);
        model.addAttribute("errors", violations);
        return "supers";
    }

    @PostMapping("addSuper")
    public String addSuper(Super hero, HttpServletRequest request) {
        String powerId = request.getParameter("powerId");
        String[] orgIds = request.getParameterValues("organizationId");

        if (powerId.equals("No Power")) {
            hero.setPower(null);
        } else {
            hero.setPower(service.getPowerById(Integer.parseInt(powerId)));
        }

        List<Organization> orgs = new ArrayList<Organization>();
        if (orgIds != null && !Arrays.stream(orgIds).anyMatch("No Organization"::equals)) {
            for (String id : orgIds) {
                orgs.add(service.getOrgById(Integer.parseInt(id)));
            }
        }
        hero.setOrganizations(orgs);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(hero);

        if (violations.isEmpty()) {
            service.addSuper(hero);
        }

        return "redirect:/supers";
    }

    @GetMapping("superDetails")
    public String superDetails(Integer id, Model model) {
        Super hero = service.getSuperById(id);
        List<Location> locations = service.getLocationsBySuper(hero);
        model.addAttribute("super", hero);
        model.addAttribute("locations", locations);
        return "superDetails";
    }

    @GetMapping("deleteSuper")
    public String deleteSuper(Integer id, Model model) {
        service.deleteSuperById(id);
        return "redirect:/supers";
    }

    @GetMapping("editSuper")
    public String editSuper(Integer id, Model model) {
        Super hero = service.getSuperById(id);
        List<Power> powers = service.getAllPowers();
        List<Organization> orgs = service.getAllOrgs();
        model.addAttribute("super", hero);
        model.addAttribute("powers", powers);
        model.addAttribute("orgs", orgs);
        return "editSuper";
    }

    @PostMapping("editSuper")
    public String performEditSuper(@Valid Super hero, HttpServletRequest request) {
        String powerId = request.getParameter("powerId");
        String[] orgIds = request.getParameterValues("organizationId");

        if (powerId.equals("No Power")) {
            hero.setPower(null);
        } else {
            hero.setPower(service.getPowerById(Integer.parseInt(powerId)));
        }

        List<Organization> orgs = new ArrayList<Organization>();
        if (orgIds != null && !Arrays.stream(orgIds).anyMatch("No Organization"::equals)) {
            for (String id : orgIds) {
                orgs.add(service.getOrgById(Integer.parseInt(id)));
            }
        }
        hero.setOrganizations(orgs);

        service.updateSuper(hero);
        return "redirect:/supers";
    }
    @PostMapping("upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes, HttpServletRequest request) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:supers";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            String pathString = path.toString();
            String fixedPath = pathString.substring(25);

            int id = Integer.parseInt(request.getParameter("id"));
            service.addImagePath(fixedPath, id);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/supers";
    }

}
