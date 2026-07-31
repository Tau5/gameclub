package uno.tau0.gameclub;

import org.springframework.beans.factory.annotation.Autowired;
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

    List<Club> adminGetAllClubs()  {
        return clubs.findAll();
    }

    List<Club> getAvailableClubs(String groupId) {
        return userService.getLoggedInUser().map(u -> {
            return clubs.findByGroup(u.group);
        }).orElseGet(List::of);
    }

    Optional<Club> getClubById(Long id) {
       var isAdmin = userService.isAdmin();
       var group = userService.getLoggedInUser().map(u -> u.group.id).orElse(null);
       var maybeClub = clubs.findById(id);
       if (maybeClub.isPresent() && (isAdmin || maybeClub.get().group.id.equals(group))) {
           return maybeClub;
        } else {
           return Optional.empty();
        }
    }

    boolean hasPermission(Club club) {
        var isAdmin = userService.isAdmin();
        var group = userService.getLoggedInUser().map(u -> u.group.id).orElse(null);
        return isAdmin || Objects.equals(group, club.group.id);
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

    boolean createClub(String name) {
        return userService.getLoggedInUser().map(u -> {
            var club = new Club(name, u.group);
            clubs.save(club);
            return true;
        }).orElse(false);
    }

    Iterable<GameDto> getBacklog(Club club) {
        return club.backlog.stream().map(GameDto::new).toList();
    }

    Iterable<GameDto> getGamesOwnedByAll(Club club) {
        var games = club.getBacklog();
        var users = userRepository.findByGroupIdIs(club.getGroup().getId());

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
