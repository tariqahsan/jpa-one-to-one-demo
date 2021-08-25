package com.example.jpa;

import com.example.jpa.model.Gender;
import com.example.jpa.model.User;
import com.example.jpa.model.UserProfile;
import com.example.jpa.repository.UserRepository;
import com.example.jpa.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Calendar;

@SpringBootApplication
public class JpaOneToOneDemoApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
    private UserProfileRepository userProfileRepository;

	public static void main(String[] args) {
		SpringApplication.run(JpaOneToOneDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Clean up database tables
		userProfileRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();

		//=========================================

		// Create a User instance
		User user = new User("Tariq", "Ahsan", "tariqahsan@yahoo.com",
				"MY_SUPER_SECRET_PASSWORD");

		Calendar dateOfBirth = Calendar.getInstance();
		dateOfBirth.set(1961, 9, 16);

		// Create a UserProfile instance
//		UserProfile userProfile = new UserProfile("(630) 728-2091", Gender.MALE, dateOfBirth.getTime(),
//				"7385 Shady Palm Dr.", "", "", "Springfield",
//				"Karnataka", "India", "560008");

		UserProfile userProfile = new UserProfile("(630)728-2091", Gender.MALE, dateOfBirth.getTime(),
				"7385 Shady Palm Dr.","", "Springfield", "VA", "22153");
		// Set child reference(userProfile) in parent entity(user)
		user.setUserProfile(userProfile);

		// Set parent reference(user) in child entity(userProfile)
		userProfile.setUser(user);

		// Save Parent Reference (which will save the child as well)
		userRepository.save(user);

		//=========================================
	}


}
