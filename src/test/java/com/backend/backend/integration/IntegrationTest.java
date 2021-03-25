package com.backend.backend.integration;

import com.backend.backend.BackendApplication;
import com.backend.backend.models.Owner;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(classes = BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Sql(scripts = "classpath:data.sql")
    @Test()
    public void testGetOwnerById() {
        ResponseEntity<Owner> owner_response_entity = this.restTemplate.getForEntity("http://localhost:" + port + "/api/owner/1", Owner.class);
        log.warn(owner_response_entity.getBody().getName());
        assertTrue(owner_response_entity.getBody().getName().equalsIgnoreCase("Harrison"));
    }

    @Test()
    public void testGetOwners() {
        ResponseEntity<Object[]> owner_response_entity = this.restTemplate.getForEntity("http://localhost:" + port + "/api/owners", Object[].class);

        Object[] objects = owner_response_entity.getBody();
        MediaType contentType = owner_response_entity.getHeaders().getContentType();
        HttpStatus statusCode = owner_response_entity.getStatusCode();

        ObjectMapper mapper = new ObjectMapper();

        List<Owner> owners = Arrays.stream(objects)
                .map(object -> mapper.convertValue(object, Owner.class))
                .collect(Collectors.toList());

        assertTrue(contentType.includes(MediaType.valueOf("application/json")));
        assertTrue(statusCode == HttpStatus.OK);
        assertTrue(owners.size() == 3);

    }

    @Test
    public void testAddOwner(){
        Owner owner = new Owner("Harrison", "Engineer");
        ResponseEntity<Owner> ownerResponseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/api/add/owner", owner, Owner.class);
        assertTrue(ownerResponseEntity.getStatusCode() == HttpStatus.CREATED);
        assertTrue(ownerResponseEntity.getBody().getName().equalsIgnoreCase("Harrison"));
        assertTrue(ownerResponseEntity.getBody().getJob().equalsIgnoreCase("Engineer"));
    }


}


