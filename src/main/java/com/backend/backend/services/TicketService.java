package com.backend.backend.services;

import com.backend.backend.models.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketService extends JpaRepository<Tickets, Long> {
}
