package com.smartcontact.manager.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartcontact.manager.entities.Contact;
import com.smartcontact.manager.entities.User;

@Repository
public interface ContactRepo extends JpaRepository<Contact, UUID>{
//	public List<Contact> getContactsByUserId(UUID uId);
	
//	for pagination
	public Page<Contact> getContactsByUserId(UUID uId, Pageable pePageable);
	
	public List<Contact> findByNameContainingAndUser(String name, User user);
}
