package com.punkyideas.cakemgr.controller;

import com.google.gson.Gson;
import com.punkyideas.cakemgr.dao.CakeService;
import com.punkyideas.cakemgr.dto.CakeDto;
import com.punkyideas.cakemgr.dto.CreateCakeDto;
import com.punkyideas.cakemgr.dto.MessageDto;
import com.punkyideas.cakemgr.dto.ValidationErrorDto;
import com.punkyideas.cakemgr.model.CakeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CakeController {
    private static final Logger LOG = LoggerFactory.getLogger(CakeController.class);
    private CakeService cakeService;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    public CakeController(CakeService cakeService) {
        this.cakeService = cakeService;
    }

    @GetMapping("/cakes")
    public List<CakeDto> getCakes() throws IOException {
        var cakes =  cakeService.findAll();
        List<CakeDto> cakeDtos = new ArrayList<>();
        for(CakeEntity cake : cakes)
            cakeDtos.add(new CakeDto(cake.getId(), cake.getTitle(), cake.getDescription(), cake.getImage()));
        return cakeDtos;
    }

    @GetMapping("/cakes/id/{cakeId}")
    public ResponseEntity<String> getCake(@PathVariable int cakeId) {
        var cake = cakeService.findById(cakeId);
        if(cake == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto("Cake not found").toJSON());
        return ResponseEntity.status(HttpStatus.OK).contentType(contentType).body(cakeToDto(cake));
    }

    @DeleteMapping("/cakes/id/{cakeId}")
    public ResponseEntity<String> deleteCake(@PathVariable int cakeId) {
        var cake = cakeService.findById(cakeId);
        if(cake == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto("Cake not found").toJSON());
        cakeService.delete(cakeId);
        return ResponseEntity.status(HttpStatus.OK).contentType(contentType)
                .body(new MessageDto("Cake with title \"" + cake.getTitle() + "\" has been deleted").toJSON());
    }

    @GetMapping("/cakes/title/{cakeTitle}")
    public ResponseEntity<String> getCake(@PathVariable String cakeTitle) {
        var gson = new Gson();
        var cake = cakeService.findByTitle(cakeTitle);
        if(cake == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto("Cake not found").toJSON());
        return ResponseEntity.status(HttpStatus.OK).contentType(contentType).body(cakeToDto(cake));
    }

    @PostMapping("/cakes")
    public ResponseEntity<String> createCake(@Valid @RequestBody CreateCakeDto cakeData) {
        if(cakeService.isCakePresentByTitle(cakeData.getTitle())) {
            var gson = new Gson();
            ValidationErrorDto[] errors = {new ValidationErrorDto("title", "Not unique")};
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(contentType).body(gson.toJson(errors));
        }
        CakeEntity cake = new CakeEntity(cakeData.getTitle(), cakeData.getDescription(), cakeData.getImage());
        var savedCake = cakeService.save(cake);
        return ResponseEntity.status(HttpStatus.OK).contentType(contentType).body(cakeToDto(savedCake));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ValidationErrorDto> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<ValidationErrorDto> errors = new ArrayList();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new ValidationErrorDto(fieldName, errorMessage));
        });
        return errors;
    }

    private String cakeToDto(CakeEntity cake) {
        return new CakeDto(cake.getId(), cake.getTitle(), cake.getDescription(), cake.getImage()).toJSON();
    }
}