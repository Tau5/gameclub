package uno.tau0.gameclub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRespository groupRespository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private UserDetails getSpringUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    
    Optional<User> getLoggedInUser() {
        UserDetails user = getSpringUser();
        return userRepository.findById(user != null ? user.getUsername() : "");
    }

    boolean isAdmin() {
        var user = getSpringUser();
        return user.getAuthorities().contains("ROLE_ADMIN");
    }

    User createUserWithDefaults(String username, String displayName, String password) {
       Group group = groupRespository.findById(1L).orElseThrow();
       User user = new User(
               username,
               displayName,
               passwordEncoder.encode(password),
               group
       );

       return userRepository.save(user);
    }
}
