package com.backend.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tickets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id", nullable=false)
    private Owner owner;

    @NotNull
    @Size(min = 10, max = 200)
    private String description;

    @NotNull
    private Boolean finished;

    public Tickets(Owner owner, @NotNull @Size(min = 10, max = 200) String description, @NotNull Boolean finished) {
        this.owner = owner;
        this.description = description;
        this.finished = finished;
    }
}
