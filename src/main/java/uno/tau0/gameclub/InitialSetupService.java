package uno.tau0.gameclub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uno.tau0.gameclub.entity.User;

import java.util.Optional;

@Service
public class InitialSetupService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    boolean isAvailable() {
       return userRepository.count() < 1;
    }


    Optional<User> registerUser(String username, String displayName, String password) {
        if (isAvailable()) {
            return Optional.of(
                    userService.createUserWithoutInvitation(username, displayName, password)
            );
        }

        return Optional.empty();
    }
}
