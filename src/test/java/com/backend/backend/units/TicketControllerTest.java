package com.backend.backend.controllers.units;

import com.backend.backend.controllers.TicketController;
import com.backend.backend.models.Owner;
import com.backend.backend.models.Tickets;
import com.backend.backend.services.TicketService;
import com.backend.backend.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @MockBean
    private TicketService ticketService;

    @MockBean
    private UserService userService;

    @Autowired
    private TicketController ticketController;

    @Autowired
    private MockMvc mockMvc;

    private List<Tickets> tickets;
    private List<Tickets> tickets2;
    private List<Tickets> tickets3;

    private List<Owner> owners;

    private Owner owner;
    private Owner owner2;
    private Owner owner3;

    @BeforeEach
    void setUp() {

        owner = new Owner(1L, "Boyns", "Charlie", null);
        owner2 = new Owner(2L, "Ceasar", "Julius", null);
        owner3 = new Owner(3L, "Reed", "Andrew", null);

        tickets = Arrays.asList(
                new Tickets(1L, owner, "Engineer", true),
                new Tickets(2L, owner, "DevOps", true),
                new Tickets(3L, owner, "ProjectManager", true));

        tickets2 = Arrays.asList(
                new Tickets(4L, owner2, "XXX", true),
                new Tickets(5L, owner2, "OOO", true));

        tickets3 = Arrays.asList(
                new Tickets(6L, owner3, "GGG", true),
                new Tickets(7L, owner3, "LLL", true),
                new Tickets(8L, owner3, "KKK", true));


        owner.setTickets(tickets);
        owner2.setTickets(tickets2);
        owner3.setTickets(tickets3);

        owners = Arrays.asList(owner, owner2, owner3);
    }

    @Test
    void getTickets() throws Exception {

        when(ticketService.findAll()).thenReturn(tickets);

        this.mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Engineer"))
                .andExpect(jsonPath("$[1].description").value("DevOps"))
                .andExpect(jsonPath("$[2].description").value("ProjectManager"))
                .andReturn();
    }

    @Test
    void getTicketsByUserId() throws Exception {

        when(userService.findById(1L)).thenReturn(java.util.Optional.ofNullable(owner));

        this.mockMvc.perform(get("/api/user/1000/tickets"))
                .andExpect(status().isNotFound())
                .andReturn();

        this.mockMvc.perform(get("/api/user/1/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Engineer"))
                .andExpect(jsonPath("$[1].description").value("DevOps"))
                .andExpect(jsonPath("$[2].description").value("ProjectManager"))
                .andReturn();
    }

    @Test
    void addTicket() throws Exception {

        when(userService.findById(1L)).thenReturn(java.util.Optional.ofNullable(owner));

        this.mockMvc.perform(get("/api/add/ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Tickets(10L, owner, "Engineer", true))))
                .andExpect(status().isMethodNotAllowed())
                .andReturn();

        this.mockMvc.perform(post("/api/add/ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Tickets(10L, owner, "Engineer", true))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Engineer"))
                .andReturn();
    }

    @Test
    void deleteTicket() throws Exception {
        when(ticketService.findById(1L)).thenReturn(java.util.Optional.ofNullable(
                new Tickets(1L, owner, "Engineer", true)));

        this.mockMvc.perform(delete("/api/delete/ticket/1"))
                .andReturn();

        verify(ticketService, times(1)).deleteById(anyLong());
    }

    @Test
    void updateTicketDescription() throws Exception {
        when(ticketService.findById(1L)).thenReturn(java.util.Optional.ofNullable(
                new Tickets(1L, owner, "Engineer", true)));
        this.mockMvc.perform(put("/api/update/ticket/1/description")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Tickets(1L, owner, "Cheese", true))))
                .andExpect(jsonPath("$.description").value("Cheese"))
                .andReturn();

        verify(ticketService, times(1)).save(any());

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

// Test Owner interactions

    @Test
    void getOwners() throws Exception {

        when(userService.findAll()).thenReturn(owners);

        this.mockMvc.perform(get("/api/owners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Boyns"))
                .andExpect(jsonPath("$[1].job").value("Julius"))
                .andReturn();
    }


    @Test
    void addUser() throws Exception {

        when(userService.findById(1L)).thenReturn(java.util.Optional.ofNullable(owner));

        this.mockMvc.perform(get("/api/add/owner")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Owner(1L, "Harrison", "Engineer"))))
                .andExpect(status().isMethodNotAllowed())
                .andReturn();

        this.mockMvc.perform(post("/api/add/owner")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Owner(1L, "Harrison", "Engineer"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Harrison"))
                .andReturn();
    }

    @Test
    void deleteOwner() throws Exception {
        when(userService.findById(1L)).thenReturn(java.util.Optional.ofNullable(
                new Owner(1L, "Harrison", "Engineer")));

        this.mockMvc.perform(delete("/api/delete/owner/1"))
                .andReturn();

        verify(userService, times(1)).deleteById(anyLong());
    }

    @Test
    void updateOwnerJob() throws Exception {
        when(userService.findById(1L)).thenReturn(java.util.Optional.ofNullable(
                new Owner(1L, "Harrison", "Engineer")));
        this.mockMvc.perform(put("/api/update/owner/1/job")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Owner(1L, "Harrison", "Cheese"))))
                .andExpect(jsonPath("$.job").value("Cheese"))
                .andReturn();

        verify(userService, times(1)).save(any());
    }
}