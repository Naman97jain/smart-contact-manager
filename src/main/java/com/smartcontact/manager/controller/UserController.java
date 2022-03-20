package com.smartcontact.manager.controller;

import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartcontact.manager.entities.Contact;
import com.smartcontact.manager.entities.User;
import com.smartcontact.manager.helper.Message;
import com.smartcontact.manager.repository.ContactRepo;
import com.smartcontact.manager.repository.UserRepo;

@RequestMapping("/user")
@Controller
public class UserController {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ContactRepo contactRepo;

	@ModelAttribute
	public void addUserData(Model model, Principal principal) {
		System.out.println("Model Attribute called");
		User user = this.userRepo.getUserByEmail(principal.getName());
		model.addAttribute("title", "Add contact");
		model.addAttribute("user", user);
	}

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		System.out.println("index page");
		User user = this.userRepo.getUserByEmail(principal.getName());
		System.out.println(user);
		model.addAttribute("user", user);
		return "user/dashboard";
	}

	@GetMapping("/add-contact")
	public String addContact(Model model) {
		model.addAttribute("contact", new Contact());
		model.addAttribute("title", "addContact");
		return "user/add-contact";
	}

	@PostMapping("/processAddContact")
	public String processAddContact(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile profileImage, Principal principal, HttpSession session) {
		System.out.println(contact);
		try {
			User user = this.userRepo.getUserByEmail(principal.getName());

			if (profileImage.isEmpty()) {
				System.out.println("File is Empty");
				contact.setImageUrl("default.png");

			} else {
				if (!this.saveUploadedFile(contact, profileImage)) {
					throw new Exception("Error in saving profile image");
				}
			}
			contact.setUser(user);
			user.getContacts().add(contact);

			this.userRepo.save(user);
			session.setAttribute("message", new Message("Contact Added Successfully", "success"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			session.setAttribute("message", new Message("Something went wrong", "danger"));
		}
//		System.out.println(user);
		return "user/add-contact";
	}

	@GetMapping("/show-contacts/{page}")
	public String showContacts(Model model, Principal principal, @PathVariable("page") Integer page) {
		String userName = principal.getName();
		User user = this.userRepo.getUserByEmail(userName);

		Pageable pageable = PageRequest.of(page, 2);

		Page<Contact> contactsByUserId = this.contactRepo.getContactsByUserId(user.getId(), pageable);
		model.addAttribute("contacts", contactsByUserId);
		model.addAttribute("totalPages", contactsByUserId.getTotalPages());
		model.addAttribute("currentPage", page);
		return "user/show-contact";
	}

	@GetMapping("/contactDetail/{contactId}")
	public String getContactDetail(Model model, @PathVariable("contactId") UUID contactId) {
		Contact contactById = this.contactRepo.getById(contactId);
		model.addAttribute("contact", contactById);
		return "user/contactDetail";
	}

	@GetMapping("/delete/{contactId}")
	public String deleteContact(@PathVariable("contactId") UUID contactId, HttpSession session) {

		try {
			Contact contactById = this.contactRepo.getById(contactId);
			contactById.setUser(null);
			System.out.println(contactId);
			if (!contactById.getImageUrl().equals("default.png")) {
				if (!this.deleteUploadedFile(contactById.getImageUrl())) {
					throw new Exception("Error in deleting uploaded profile pic.");
				}
			}

			this.contactRepo.delete(contactById);

			session.setAttribute("message", new Message("Contact Deleted Successfully", "success"));
		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong while deleting the contact", "danger"));
		}
		return "redirect:/user/show-contacts/0";
	}

	@GetMapping("/updateContact/{contactId}")
	public String UpdateContact(Model model, @PathVariable("contactId") UUID contactId) {
		Contact contactById = this.contactRepo.getById(contactId);
		model.addAttribute("contact", contactById);
		model.addAttribute("title", "updateContact");
		return "user/add-contact";
	}

	@PostMapping("/processUpdateContact")
	public String processUpdateContact(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile profileImage, HttpSession session) {
		try {
			Contact savedContactInDB = this.contactRepo.getById(contact.getcId());
			if (!profileImage.isEmpty()) {
				if (!contact.getImageUrl().equals("default.png")) {
					if (!this.deleteUploadedFile(contact.getImageUrl())) {
						throw new Exception("Error in deleting uploaded profile pic.");
					} else {
						if (!this.saveUploadedFile(contact, profileImage)) {
							throw new Exception("Error while updating profile pic.");
						}
					}
				}
				contact.setImageUrl(profileImage.getOriginalFilename());
			}
			else {
				contact.setImageUrl(savedContactInDB.getImageUrl());
			}
			
			contact.setUser(savedContactInDB.getUser());
			Contact updatedContact = this.contactRepo.save(contact);
			System.out.println(updatedContact);
			session.setAttribute("message", new Message("Contact updated successfully", "success"));
		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong while updating contact", "danger"));
		}
		return "redirect:/user/contactDetail/" + contact.getcId();
	}
	
	@GetMapping("/profile")
	public String getProfile(Model model, Principal principal) {
		User user = this.userRepo.getUserByEmail(principal.getName());
		model.addAttribute("user", user);
		return "user/profile";
	}
	

	public boolean deleteUploadedFile(String imageUrl) {
		try {
			File fileToDelete = new ClassPathResource("static/image").getFile();

			Path path = Paths.get(fileToDelete.getAbsolutePath() + File.separator + imageUrl);

			Files.delete(path);
		} catch (Exception e) {
			System.out.println("Exception occured while deleting file");
			return false;
		}
		return true;
	}

	public boolean saveUploadedFile(Contact contact, MultipartFile profileImage) {
		try {
			contact.setImageUrl(profileImage.getOriginalFilename());

			File fileToSave = new ClassPathResource("static/image").getFile();

			Path path = Paths.get(fileToSave.getAbsolutePath() + File.separator + profileImage.getOriginalFilename());
			System.out.println("path: " + path);
			Files.copy(profileImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;

	}

}
