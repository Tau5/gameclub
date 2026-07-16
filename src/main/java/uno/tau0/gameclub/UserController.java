package uno.tau0.gameclub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/*
ENDPOINTS

GET /api/v1/users
GET /api/v1/users/role
GET /api/v1/users/group
GET /api/v1/users/{id}/games
POST /api/v1/users
POST /api/v1/users/{id}/games
PATCH /api/v1/users/{id}

 */

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    UserRepository users;

    UserController(UserRepository repo) {
       this.users = repo;
    }

    record UserDto(
        String username,
        String displayName,
        String role
    ) {
        UserDto(User u) {
            this(u.name, u.displayName, u.role);
        }
    }

    @GetMapping()
    public Iterable<UserDto> getUsers() {
        return users.findAll().stream().map(UserDto::new).toList();
    }

    @GetMapping("me")
    public Optional<UserDto> getLoggedUser() {
        return userService.getLoggedInUser().map(UserDto::new);
    }

    record NewUser (
       String username,
       String displayName,
       String password
    ) { }

    @PostMapping()
    public UserDto createUser(@RequestBody NewUser userData) {
        return new UserDto(userService.createUserWithDefaults(
               userData.username,
               userData.displayName,
               userData.password
        ));
    }
}
