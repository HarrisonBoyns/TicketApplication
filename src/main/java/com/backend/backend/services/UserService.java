package com.backend.backend.services;

import com.backend.backend.models.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserService extends JpaRepository<Owner, Long> {
}
