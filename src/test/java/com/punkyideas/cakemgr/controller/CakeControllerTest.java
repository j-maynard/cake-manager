package com.punkyideas.cakemgr.controller;

import com.google.gson.Gson;
import com.punkyideas.cakemgr.CakeLoader;
import com.punkyideas.cakemgr.dao.CakeService;
import com.punkyideas.cakemgr.dto.CakeDto;
import com.punkyideas.cakemgr.dto.CreateCakeDto;
import com.punkyideas.cakemgr.dto.MessageDto;
import com.punkyideas.cakemgr.dto.ValidationErrorDto;
import com.punkyideas.cakemgr.model.CakeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations="classpath:application-test.properties")
class CakeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private CakeService cakeService;

    @MockBean
    private CakeLoader cakeLoader;

    private Gson gson = new Gson();

    private CakeEntity testCake = CakeEntity.testCakeBuilder(
            1,
            "Not Cake",
            "The Cake is a Lie",
            "http://localhost:9002/images/not_cake_image.png"
            );

    @BeforeEach
    public void setUp() {
        var allCakes = new ArrayList<CakeEntity>();
        allCakes.add(testCake);
        when(cakeService.findAll())
                .thenReturn(allCakes);
       when(cakeService.findById(testCake.getId()))
                .thenReturn(testCake);
        when(cakeService.findByTitle(testCake.getTitle()))
                .thenReturn(testCake);
        when(cakeService.save(new CakeEntity(testCake.getTitle(), testCake.getDescription(), testCake.getImage())))
                .thenReturn(testCake);
        when(cakeService.save(new CakeEntity("Unique Test Cake", testCake.getDescription(), testCake.getImage())))
                .thenReturn(testCake);
        when(cakeService.isCakePresentByTitle(testCake.getTitle()))
                .thenReturn(true);
        when(cakeService.isCakePresentByTitle("Unique Test Cake"))
                .thenReturn(false);
    }

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Test
    public void getAllCakes() throws Exception {
        MvcResult result = mvc.perform(get("/cakes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();
        CakeDto expectedCakes[] = {
                new CakeDto(testCake.getId(), testCake.getTitle(), testCake.getDescription(), testCake.getImage())
        };

        var resultCakes = gson.fromJson(result.getResponse().getContentAsString(), CakeDto[].class);
        assertEquals(expectedCakes.length, resultCakes.length);
        assertEquals(expectedCakes[0], resultCakes[0]);
    }

    @Test
    public void getCakeById() throws Exception {
        MvcResult result = mvc.perform(get("/cakes/id/" + testCake.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();
        var expectedCake = new CakeDto(testCake.getId(), testCake.getTitle(), testCake.getDescription(), testCake.getImage());
        var resultCake = gson.fromJson(result.getResponse().getContentAsString(), CakeDto.class);
        assertEquals(resultCake, expectedCake);
    }

    @Test
    public void getCakeByTitle() throws Exception {
        MvcResult result = mvc.perform(get("/cakes/title/" + testCake.getTitle())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();
        var expectedCake = new CakeDto(testCake.getId(), testCake.getTitle(), testCake.getDescription(), testCake.getImage());
        var resultCake = gson.fromJson(result.getResponse().getContentAsString(), CakeDto.class);
        assertEquals(resultCake, expectedCake);
    }

    @Test
    public void createCake() throws Exception {
        var cakeToPost = new CreateCakeDto("Unique Test Cake", testCake.getDescription(), testCake.getImage());
        MvcResult result = mvc.perform(post("/cakes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(gson.toJson(cakeToPost)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();
        var expectedCake = new CakeDto(testCake.getId(), testCake.getTitle(), testCake.getDescription(), testCake.getImage());
        var resultCake = gson.fromJson(result.getResponse().getContentAsString(), CakeDto.class);
        assertEquals(resultCake, expectedCake);
    }

    @Test
    public void createCakeWithDuplicateTitleReturns400() throws Exception {
        var cakeToPost = new CreateCakeDto(testCake.getTitle(), testCake.getDescription(), testCake.getImage());
        MvcResult result = mvc.perform(post("/cakes")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(gson.toJson(cakeToPost)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andReturn();
        ValidationErrorDto[] expectedErrorObj = {new ValidationErrorDto("title", "Not unique")};
        var expectedError = gson.toJson(expectedErrorObj);
        var resultError = result.getResponse().getContentAsString();
        assertEquals(resultError, expectedError);
    }

    @Test
    public void deleteCake() throws Exception {
        MvcResult result = mvc.perform(delete("/cakes/id/" + testCake.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();
        var expectedMessage = new MessageDto("Cake with title \"" + testCake.getTitle() + "\" has been deleted");
        var resultMessage = gson.fromJson(result.getResponse().getContentAsString(), MessageDto.class);
        assertEquals(resultMessage, expectedMessage);
    }
}