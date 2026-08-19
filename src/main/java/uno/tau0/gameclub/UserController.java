package uno.tau0.gameclub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uno.tau0.gameclub.dto.GameDto;
import uno.tau0.gameclub.dto.InvitationDto;
import uno.tau0.gameclub.dto.JoinClubRequest;
import uno.tau0.gameclub.dto.RegisterRequest;
import uno.tau0.gameclub.entity.Invitation;
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

    @Autowired
    InvitationService invitationService;

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

    @PostMapping("me/invitations")
    @ResponseStatus(HttpStatus.CREATED)
    public InvitationDto generateInvitation() throws InvitationService.InvitationLimitReachedException {
        var user = userService.getLoggedInUser().orElseThrow();
        return InvitationDto.from(invitationService.generateInvitation(user));
    }

    @GetMapping("me/invitations")
    public Iterable<InvitationDto> listInvitations() {
        var user = userService.getLoggedInUser().orElseThrow();
        return InvitationDto.from(invitationService.getInvitationsOfUser(user));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public String invitationLimitReached(InvitationService.InvitationLimitReachedException ex) {
        return "Invitation limit reached";
    }

    //@PostMapping()
    //public UserDto createUser(@RequestBody RegisterRequest userData) throws InvitationService.InvalidInvitationException, InvitationService.InvitationNotFoundException {
    //    return new UserDto(userService.createUser(
    //        userData.username(),
    //        userData.displayName(),
    //        userData.password(),
    //        userData.invitationCode()
    //    ));
    //}
}
