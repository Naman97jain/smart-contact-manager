package com.smartcontact.manager.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.smartcontact.manager.entities.User;
import com.smartcontact.manager.helper.Message;
import com.smartcontact.manager.repository.UserRepo;

//import com.smartcontact.manager.entities.User;
//import com.smartcontact.manager.repository.UserRepo;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	@Autowired
	private UserRepo userRepo;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About");
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "SignUp");
		User user = new User();
		model.addAttribute("user", user);
		return "user/signup";
	}
	
	/**sign-in is the default url provided by spring security
	 * we can modify this in the MyConfig file by adding .loginProcessingUrl("custom-url")
	 * after formLogin().
	 * **/
//	@PostMapping("/signin")
//	public RedirectView login(Model model) {
//		model.addAttribute("title", "Login");
//		RedirectView redirectView = new RedirectView();
//		redirectView.setUrl("/user/index");
//		return redirectView;
//	}

	@PostMapping("/process-signup")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult validationResult,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, 
			Model model,
			HttpSession session) {
		System.out.println(agreement);
		System.out.println(validationResult.hasErrors());
		System.out.println(user);
		
		
		if (validationResult.hasErrors()){
			model.addAttribute("user", user);
			return "user/signup";
		}
		try {
			if (!agreement) {
				System.out.println("User did not agree to terms and conditions");
				throw new Exception("Please accept terms and conditions to proceed");
			}
			user.setEnabled(true);
			user.setRole("ROLE_USER");
			user.setImageUrl("/image/default.png");
			user.setPassword(this.bcryptPasswordEncoder.encode(user.getPassword()));
			User result = this.userRepo.save(user);
			model.addAttribute("user", result);
			session.setAttribute("message", new Message("Successfully Registered", "alert-success"));

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong!! " + e.getMessage(), "alert-danger"));
		}

		return "user/signup";
	}

}
