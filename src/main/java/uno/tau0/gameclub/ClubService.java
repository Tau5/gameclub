package uno.tau0.gameclub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uno.tau0.gameclub.dto.GameDto;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClubService {
    @Autowired
    private ClubRepository clubs;

    @Autowired
    private GameRepository games;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    List<Club> adminGetAllClubs()  {
        return clubs.findAll();
    }

    List<Club> getAvailableClubs() {
        return userService.getLoggedInUser().map(u -> {
            return u.clubs.stream().toList();
        }).orElseGet(List::of);
    }

    Optional<Club> getClubById(Long id) {
       var maybeClub = clubs.findById(id);
       if (maybeClub.isPresent() && hasPermission(maybeClub.get())) {
           return maybeClub;
        } else {
           return Optional.empty();
        }
    }

    boolean hasPermission(Club club) {
        var isAdmin = userService.isAdmin();
        var userIsInClub = userService.getLoggedInUser().stream().anyMatch(u -> {
            return u.clubs.stream().anyMatch(c -> c.id.equals(club.id));
        });
        return isAdmin || userIsInClub;
    }

    void addGameToBacklog(Club club, Game game) {
        if (!hasPermission(club)) return;
        if (!club.backlog.stream().anyMatch(g -> g.getId() == game.getId())) {
            club.backlog.add(game);
            clubs.save(club);
        }
    }

    void removeGameFromBacklog(Long clubId, Long gameId) {
        Club club = getClubById(clubId).orElseThrow();
        Game game = games.findById(gameId).orElseThrow();

        if (!hasPermission(club)) return;
        club.backlog.removeIf(g -> g.getId().equals(game.getId()));
        clubs.save(club);
    }

    boolean createClub(String name, String password) {
        return userService.getLoggedInUser().map(u -> {
            var club = new Club(name, passwordEncoder.encode(password));
            clubs.save(club);
            return true;
        }).orElse(false);
    }

    Iterable<GameDto> getBacklog(Club club) {
        return club.backlog.stream().map(GameDto::new).toList();
    }

    Iterable<GameDto> getGamesOwnedByAll(Club club) {
        var games = club.getBacklog();
        var users = club.getMembers();

        return games.stream().filter(g ->
            users.stream().allMatch(
                    u -> u.ownedGames.stream().anyMatch(o -> o.getId().equals(g.getId()))
            )
        ).map(GameDto::new).toList();
    }

    void setCurrentGame(Long clubId, Long gameId) {
        Club club = getClubById(clubId).orElseThrow();
        Game game = games.findById(gameId).orElseThrow();

        club.setCurrentGame(game);
        clubs.save(club);
    }
}
