package com.backend.backend;

import com.backend.backend.exceptionscustom.ApiException;
import com.backend.backend.models.Owner;
import com.backend.backend.models.Tickets;
import com.backend.backend.services.TicketService;
import com.backend.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
@Validated
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @GetMapping(path = "/ticket/{id}")
    public ResponseEntity<Tickets> getTicket(@PathVariable Long id){
        Tickets tickets = ticketService.findById(id).get();
        return new ResponseEntity<Tickets>(tickets, HttpStatus.OK);
    }

    @GetMapping(path = "/tickets")
    public ResponseEntity<Collection<Tickets>> getTickets(){
        return new ResponseEntity<Collection<Tickets>>(ticketService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/user/{userid}/tickets")
    public ResponseEntity<List<Tickets>> getTicketsByUserId(@PathVariable Long userid) throws ApiException {
        Owner owner = userService.findById(userid).orElseThrow(() -> new ApiException("No user of id:" + userid, HttpStatus.NOT_FOUND));
        List<Tickets> tickets = owner.getTickets();
        return new ResponseEntity<List<Tickets>>(tickets, HttpStatus.OK);
    }

    @PostMapping(path = "/add/ticket")
    public ResponseEntity<Tickets> addTicket(@RequestBody Tickets ticket) throws ApiException {
        Owner owner = userService.findById(ticket.getOwner().getId()).orElseThrow(() -> new ApiException("No user of this id", HttpStatus.NOT_FOUND));
        ticketService.save(ticket);
        return new ResponseEntity<>(ticket, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/delete/ticket/{id}")
    public ResponseEntity<Tickets> deleteTicket(@PathVariable Long id) throws ApiException {
        Tickets tickets = ticketService.findById(id).orElseThrow(() -> new ApiException("No ticket of id: " + id, HttpStatus.NOT_FOUND));
        ticketService.deleteById(id);
        return new ResponseEntity<Tickets>(tickets, HttpStatus.OK);
    }

    @PutMapping(path = "/update/ticket/{id}/description")
    public ResponseEntity<Tickets> updateTicketDescription(@PathVariable Long id, @RequestBody Tickets tickets) throws ApiException {
        Tickets db_ticket = ticketService.findById(id).orElseThrow(() -> new ApiException("No ticket of id: " + id, HttpStatus.NOT_FOUND));;
        db_ticket.setDescription(tickets.getDescription());
        ticketService.save(db_ticket);
        return new ResponseEntity<Tickets>(db_ticket, HttpStatus.OK);
    }

//    User Functionality

    @GetMapping(path = "/owner/{id}")
    public ResponseEntity<Owner> getOwnerById(@PathVariable Long id){
        Owner owner = userService.findById(id).orElseThrow( () -> new ApiException("No user found of id:" + id, HttpStatus.NOT_FOUND));
        return new ResponseEntity<Owner>(owner, HttpStatus.OK);
    }

    @GetMapping(path = "/owners")
    public ResponseEntity<Collection<Owner>> getOwners(){
        return new ResponseEntity<Collection<Owner>>(userService.findAll(), HttpStatus.OK);
    }

    @PostMapping(path = "/add/owner")
    public ResponseEntity<Owner> addOwner(@RequestBody Owner owner) throws ApiException {
        userService.save(owner);
        return new ResponseEntity<Owner>(owner, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/delete/owner/{id}")
    public ResponseEntity<Owner> deleteOwner(@PathVariable Long id) throws ApiException {
        Owner owner = userService.findById(id).orElseThrow(EntityNotFoundException::new);
        userService.deleteById(id);
        if(userService.existsById(id)) {
            return new ResponseEntity<Owner>(owner, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Owner>(owner, HttpStatus.OK);
    }

    @PutMapping(path = "/update/owner/{id}/job")
    public ResponseEntity<Owner> updateOwnerJob(@PathVariable Long id, @RequestBody Owner owner) throws ApiException {
        Owner owner_db = userService.findById(id).orElseThrow(() -> new ApiException("No ticket of id: " + id, HttpStatus.NOT_FOUND));;
        owner_db.setJob(owner.getJob());
        userService.save(owner_db);
        return new ResponseEntity<Owner>(owner_db, HttpStatus.OK);
    }



}
