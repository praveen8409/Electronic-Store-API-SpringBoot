package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
