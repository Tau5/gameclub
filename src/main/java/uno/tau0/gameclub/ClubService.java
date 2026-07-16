package uno.tau0.gameclub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClubService {
    @Autowired
    private ClubRepository clubs;

    @Autowired
    private UserService userService;

    Iterable<Club> adminGetAllClubs()  {
        return clubs.findAll();
    }

    Iterable<Club> getAvailableClubs() {
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

    void addGameToBacklog(Club club, Game game) {
        club.backlog.add(game);
        clubs.save(club);
    }

    boolean createClub(String name) {
        return userService.getLoggedInUser().map(u -> {
            var club = new Club(name, u.group);
            clubs.save(club);
            return true;
        }).orElse(false);
    }
}
