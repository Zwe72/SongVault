package harjoitustyo.songvault;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import harjoitustyo.songvault.model.User;
import harjoitustyo.songvault.model.UserRepository;

@SpringBootApplication
public class SongvaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(SongvaultApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepository userRepository) {
		return (args) -> {
			userRepository.save(new User("user", "{noop}password", "USER"));
		};
	}

}