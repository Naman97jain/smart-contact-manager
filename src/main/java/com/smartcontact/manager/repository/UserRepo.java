 package com.smartcontact.manager.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartcontact.manager.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, UUID>{
	public User getUserByEmail(String email);
}
