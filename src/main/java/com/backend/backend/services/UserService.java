package com.backend.backend.services;

import com.backend.backend.models.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserService extends JpaRepository<Owner, Long> {
}
