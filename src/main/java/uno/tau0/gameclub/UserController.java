package uno.tau0.gameclub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uno.tau0.gameclub.dto.GameDto;
import uno.tau0.gameclub.dto.JoinClubRequest;
import uno.tau0.gameclub.entity.User;

import java.util.List;
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

    @Autowired
    ClubService clubService;

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
            this(u.name,
                    u.displayName,
                    u.role
            );
        }
    }

    @GetMapping("me")
    public Optional<UserDto> getLoggedUser() {
        return userService.getLoggedInUser().map(UserDto::new);
    }

    @GetMapping("me/games")
    public Iterable<GameDto> getOwnedGames() {
        return userService.getLoggedInUser().map(u ->
                userService.getOwnedGames(u)
        ).orElse(List.of());
    }

    @PostMapping("me/games")
    @ResponseStatus(HttpStatus.CREATED)
    public void addOwnedGame(@RequestParam Long gameId) throws Exception {
        User user = userService.getLoggedInUser().orElseThrow();
        userService.addOwnedGame(user, gameId);
    }

    @DeleteMapping("me/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeOwnedGame(@PathVariable Long gameId) throws Exception {
        User user = userService.getLoggedInUser().orElseThrow();
        userService.removeOwnedGame(user, gameId);
    }

    @PostMapping("me/clubs")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> joinClub(@RequestBody JoinClubRequest request) {
        if (clubService.joinClub(request.clubName(), request.password())) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully joined club");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to join club");
        }
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
