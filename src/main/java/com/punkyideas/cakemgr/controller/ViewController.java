package com.punkyideas.cakemgr.controller;

import com.punkyideas.cakemgr.dao.CakeService;
import com.punkyideas.cakemgr.dto.CreateCakeDto;
import com.punkyideas.cakemgr.model.CakeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ViewController {

    private static final Logger LOG = LoggerFactory.getLogger(ViewController.class);

    private CakeService cakeService;

    @Autowired
    public ViewController(CakeService cakeService) {
        this.cakeService = cakeService;
    }

    @GetMapping("/")
    public String showCakes(Model model) {
        var cakes = cakeService.findAll();
        var col = true;
        List<CakeEntity> col1 = new ArrayList<>();
        List<CakeEntity> col2 = new ArrayList<>();
        for(var cake: cakes) {
            if(!validateImageUrl(cake.getImage()))
                cake.setImage("/images/missing_cake.png");
            if(col) {
                col = false;
                col1.add(cake);
            } else {
                col = true;
                col2.add(cake);
            }
        }
        model.addAttribute("cakes", cakes);
        model.addAttribute("col1", col1);
        model.addAttribute("col2", col2);
        return "index.html";
    }

    @GetMapping("/addcake")
    public String cakeForm(CreateCakeDto createCakeDto) {
        return "addcake.html";
    }

    @PostMapping("/addcake")
    public String processCakeForm(@Valid CreateCakeDto createCakeDto, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            return "addcake.html";
        }
        if(!validateImageUrl(createCakeDto.getImage())) {
            bindingResult.rejectValue("image", "error.createCakeDto", "Image must be a valid working url.");
            return "addcake.html";
        }
        if(cakeService.findByTitle(createCakeDto.getTitle()) != null) {
            bindingResult.rejectValue("title", "error.createCakeDto", "There is already a cake with that title.");
            return "addcake.html";
        }
        CakeEntity cake = new CakeEntity(createCakeDto.getTitle(), createCakeDto.getDescription(), createCakeDto.getImage());
        cake = cakeService.save(cake);
        redirectAttrs.addFlashAttribute("successMessage", "Cake added successfully");
        return "redirect:/";
    }

    private boolean validateImageUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            int responseCode = huc.getResponseCode();
            if(responseCode != 200)
                return false;
        } catch(Exception e) {
            LOG.error("An error occurred trying to get Image URL " + imageUrl);
            return false;
        }
        return true;
    }

}
