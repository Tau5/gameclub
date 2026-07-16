package uno.tau0.gameclub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ClubDTO {
    Long id;
    String name;
}

@RestController
@RequestMapping("/clubs")
public class ClubController {

    @Autowired
    private ClubService clubs;

    @Autowired
    private GameRepository games;

    @GetMapping("/admin/all")
    public Iterable<Club> findAll() {
        return clubs.adminGetAllClubs();
    }

    @GetMapping()
    public Iterable<Club> findAllAvailable() {
        return clubs.getAvailableClubs();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HttpStatus create(@RequestBody String name) {
        if (clubs.createClub(name)) {
            return HttpStatus.CREATED;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @PostMapping("/{id}/backlog")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Iterable<Game>> addGame(@PathVariable Long clubId, @RequestBody Long gameId) {
        var maybeClub = clubs.getClubById(clubId);
        var maybeGame = games.findById(gameId);
        if (maybeClub.isPresent() && maybeGame.isPresent()) {
            var club = maybeClub.get();
            clubs.addGameToBacklog(club, maybeGame.get());
            return ResponseEntity.status(HttpStatus.OK).body(club.backlog);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
