package uno.tau0.gameclub;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uno.tau0.gameclub.dto.ClubCreateRequest;
import uno.tau0.gameclub.dto.GameDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

record ClubDTO (
    Long id,
    String name
) {
    ClubDTO(Club club) {
        this(club.id, club.name);
    }
}

@RestController
@RequestMapping("/clubs")
public class ClubController {

    @Autowired
    private ClubService clubs;

    @Autowired
    private GameRepository games;

    @GetMapping("/admin/all")
    public Iterable<ClubDTO> findAll() {
        return clubs.adminGetAllClubs().stream().map(ClubDTO::new).toList();
    }

    @GetMapping()
    public Iterable<ClubDTO> findAllAvailable() {
        return clubs.getAvailableClubs().stream().map(ClubDTO::new).toList();
    }

    @GetMapping("{id}")
    public Optional<ClubDTO> findById(@PathVariable Long id) {
        return clubs.getClubById(id).map(ClubDTO::new);
    }

    @GetMapping("{id}/backlog")
    public Optional<Iterable<GameDto>> clubBacklog(@PathVariable Long id) {
        return clubs.getClubById(id).map(clubs::getBacklog);
    }

    @GetMapping("{id}/currentGame")
    public Optional<GameDto> clubCurrentGame(@PathVariable Long id) {
        return clubs.getClubById(id).map(c -> new GameDto(c.currentGame));
    }


    @PatchMapping("{id}/currentGame")
    public void clubCurrentGame(@PathVariable Long id, @RequestParam Long gameId) {
        clubs.setCurrentGame(id, gameId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HttpStatus create(@RequestBody ClubCreateRequest club) {
        if (clubs.createClub(club.name())) {
            return HttpStatus.CREATED;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @PostMapping("/{id}/backlog")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Iterable<GameDto>> addGame(@PathVariable Long id, @RequestParam Long gameId) {
        var maybeClub = clubs.getClubById(id);
        var maybeGame = games.findById(gameId);
        if (maybeClub.isPresent() && maybeGame.isPresent()) {
            var club = maybeClub.get();
            clubs.addGameToBacklog(club, maybeGame.get());
            return ResponseEntity.status(HttpStatus.OK).body(clubs.getBacklog(club));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @DeleteMapping("/{id}/backlog")
    @ResponseStatus(HttpStatus.OK)
    public void removeGame(@PathVariable Long id, @RequestParam Long gameId) {
        clubs.removeGameFromBacklog(id, gameId);
    }

    @GetMapping("/{id}/gamesEveryone")
    public Iterable<GameDto> getGamesOwnedByEveryone(@PathVariable Long id) {
       var club = clubs.getClubById(id).orElseThrow();
       return clubs.getGamesOwnedByAll(club);
    }
}
