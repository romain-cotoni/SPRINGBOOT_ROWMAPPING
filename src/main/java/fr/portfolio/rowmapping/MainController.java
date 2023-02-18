package fr.portfolio.rowmapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController 
{
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/username/{username}")
	public User getUsername(@PathVariable("username") String username)
	{
		return userRepository.findByUser(username);
	}
	
	@GetMapping("/usernames")
	public List<User> getAllUsernames()
	{
		return userRepository.findAllUsers();
	}
			
}

