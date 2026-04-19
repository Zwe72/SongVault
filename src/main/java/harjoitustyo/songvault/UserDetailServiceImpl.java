package harjoitustyo.songvault;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import harjoitustyo.songvault.model.User;
import harjoitustyo.songvault.model.UserRepository;


@Service
public class UserDetailServiceImpl implements UserDetailsService {
    
    private final UserRepository repository;

    public UserDetailServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
        User user = repository.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        
		return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();
	}
}

