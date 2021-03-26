package com.backend.backend.integration;

import com.backend.backend.BackendApplication;
import com.backend.backend.models.Owner;
import com.backend.backend.models.Tickets;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@ActiveProfiles("test")
@SpringBootTest(classes = BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class IntegrationTest {

    @LocalServerPort
    private int port;

    private final String localHost = "http://localhost:";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAddTicket(){
        ResponseEntity<Owner> owner_response_entity = this.restTemplate.getForEntity(localHost + port + "/api/owner/2", Owner.class);
        Tickets tickets = new Tickets(owner_response_entity.getBody(), "Description", true);

        ResponseEntity<Tickets> ownerResponseEntity = this.restTemplate.postForEntity(localHost + port + "/api/add/ticket", tickets, Tickets.class);

        assertTrue(ownerResponseEntity.getStatusCode() == HttpStatus.CREATED);
        assertTrue(ownerResponseEntity.getBody().getDescription().equalsIgnoreCase("Description"));
        assertTrue(ownerResponseEntity.getBody().getFinished() == true);
    }


    @Test
    public void testGetTickets() {
        ResponseEntity<Object[]> tickets_response_entity = this.restTemplate.getForEntity(localHost + port + "/api/tickets", Object[].class);

        Object[] objects = tickets_response_entity.getBody();
        MediaType contentType = tickets_response_entity.getHeaders().getContentType();
        HttpStatus statusCode = tickets_response_entity.getStatusCode();

        ObjectMapper mapper = new ObjectMapper();

        List<Tickets> tickets = Arrays.stream(objects)
                .map(object -> mapper.convertValue(object, Tickets.class))
                .collect(Collectors.toList());
        log.warn(String.valueOf(tickets.size()));

        assertTrue(contentType.includes(MediaType.valueOf("application/json")));
        assertTrue(statusCode == HttpStatus.OK);
        assertTrue(tickets.size() == 5);

    }

    @Test()
    public void testGetTicketById() {
        ResponseEntity<Tickets> owner_response_entity = this.restTemplate.getForEntity(localHost + port + "/api/ticket/1", Tickets.class);
        assertTrue(owner_response_entity.getBody().getDescription().equalsIgnoreCase("Create a react front end"));
    }

    @Test()
    public void testPutTicket() {
        ResponseEntity<Owner> owner_response_entity = this.restTemplate.getForEntity(localHost + port + "/api/owner/1", Owner.class);
        Tickets tickets = new Tickets(owner_response_entity.getBody(),"XXXXXXXXXXXXXXXXXXXX", false);

        this.restTemplate.put(localHost + port + "/api/update/ticket/2/description", tickets, Tickets.class);

        ResponseEntity<Tickets> updatedTicket = this.restTemplate.getForEntity(localHost + port + "/api/ticket/2", Tickets.class);
        assertTrue(updatedTicket.getBody().getDescription().equalsIgnoreCase("XXXXXXXXXXXXXXXXXXXX"));
    }

    @Test()
    public void testDeleteTicketById() {
        this.restTemplate.delete(localHost + port + "/api/delete/ticket/1", Tickets.class);
        ResponseEntity<Tickets> ticket_response_entity = this.restTemplate.getForEntity(localHost + port + "/api/ticket/1", Tickets.class);
        assertTrue(ticket_response_entity.getStatusCode() == HttpStatus.NOT_FOUND);
    }


// Owner tests

    @Test
    public void testAddOwner(){
        Owner owner = new Owner("Harrison", "Engineer");
        ResponseEntity<Owner> ownerResponseEntity = this.restTemplate.postForEntity(localHost + port + "/api/add/owner", owner, Owner.class);
        assertTrue(ownerResponseEntity.getStatusCode() == HttpStatus.CREATED);
        assertTrue(ownerResponseEntity.getBody().getName().equalsIgnoreCase("Harrison"));
        assertTrue(ownerResponseEntity.getBody().getJob().equalsIgnoreCase("Engineer"));
    }


    @Test
    public void testGetOwners() {
        ResponseEntity<Object[]> owner_response_entity = this.restTemplate.getForEntity(localHost + port + "/api/owners", Object[].class);

        Object[] objects = owner_response_entity.getBody();
        MediaType contentType = owner_response_entity.getHeaders().getContentType();
        HttpStatus statusCode = owner_response_entity.getStatusCode();

        ObjectMapper mapper = new ObjectMapper();

        List<Owner> owners = Arrays.stream(objects)
                .map(object -> mapper.convertValue(object, Owner.class))
                .collect(Collectors.toList());
        log.warn(String.valueOf(owners.size()));

        assertTrue(contentType.includes(MediaType.valueOf("application/json")));
        assertTrue(statusCode == HttpStatus.OK);
        assertTrue(owners.size() == 3);

    }

//    @Sql(scripts = "classpath:data.sql")
//    @Sql(scripts = "classpath:clean-up.sql", executionPhase = AFTER_TEST_METHOD)
    @Test()
    public void testGetOwnerById() {
        ResponseEntity<Owner> owner_response_entity = this.restTemplate.getForEntity(localHost + port + "/api/owner/2", Owner.class);
        System.out.println(owner_response_entity.getBody().getName());

        assertTrue(owner_response_entity.getBody().getName().equalsIgnoreCase("Charlie"));
    }

    @Test()
    public void testPutOwner() {
        Owner owner = new Owner("Testing", "123");
        this.restTemplate.put(localHost + port + "/api/update/owner/1/job", owner, Owner.class);

        ResponseEntity<Owner> owner_response_entity = this.restTemplate.getForEntity(localHost + port + "/api/owner/1", Owner.class);
        assertTrue(owner_response_entity.getBody().getName().equalsIgnoreCase("Harrison"));
        System.out.println(owner_response_entity.getBody().getJob());
        assertTrue(owner_response_entity.getBody().getJob().equalsIgnoreCase("123"));
    }

    @Test()
    public void testDeleteOwnerById() {
        this.restTemplate.delete(localHost + port + "/api/delete/owner/1", Owner.class);
        ResponseEntity<Owner> owner_response_entity = this.restTemplate.getForEntity(localHost + port + "/api/owner/1", Owner.class);
        assertTrue(owner_response_entity.getStatusCode() == HttpStatus.NOT_FOUND);
    }
}


