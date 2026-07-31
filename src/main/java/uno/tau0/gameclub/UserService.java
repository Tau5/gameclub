package uno.tau0.gameclub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uno.tau0.gameclub.dto.GameDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GameRepository games;

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
       User user = new User(
               username,
               displayName,
               passwordEncoder.encode(password)
       );

       return userRepository.save(user);
    }

    void addOwnedGame(User user, Long gameId) throws Exception {
        var game = games.findById(gameId).orElseThrow();
        user.ownedGames.add(game);
        userRepository.save(user);
    }

    void removeOwnedGame(User user, Long gameId) throws Exception {
        var game = games.findById(gameId).orElseThrow();
        user.ownedGames.remove(game);
        userRepository.save(user);
    }

    List<GameDto> getOwnedGames(User user) {
        return user.ownedGames
                .stream()
                .map(GameDto::new)
                .toList();
    }

}
