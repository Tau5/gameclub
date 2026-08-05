package uno.tau0.gameclub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uno.tau0.gameclub.dto.ClubCreateRequest;
import uno.tau0.gameclub.dto.GameDto;
import uno.tau0.gameclub.entity.Club;

import java.util.Optional;

record ClubDTO (
    String name,
    String displayName
) {
    ClubDTO(Club club) {
        this(club.getName(), club.getDisplayName());
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

    @GetMapping("{name}")
    public Optional<ClubDTO> findById(@PathVariable String name) {
        return clubs.getClubById(name).map(ClubDTO::new);
    }

    @GetMapping("{name}/backlog")
    public Optional<Iterable<GameDto>> clubBacklog(@PathVariable String name) {
        return clubs.getClubById(name).map(clubs::getBacklog);
    }

    @GetMapping("{name}/currentGame")
    public Optional<GameDto> clubCurrentGame(@PathVariable String name) {
        return clubs.getClubById(name).flatMap(c -> {
            if (c.getCurrentGame() != null) {
                return Optional.of(new GameDto(c.getCurrentGame()));
            } else {
                return Optional.empty();
            }
        });
    }

    @PatchMapping("{name}/currentGame")
    public void clubCurrentGame(@PathVariable String name, @RequestParam Long gameId) {
        clubs.setCurrentGame(name, gameId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HttpStatus create(@RequestBody ClubCreateRequest club) {
        if (clubs.createClub(club.name(), club.displayName(), club.password())) {
            return HttpStatus.CREATED;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @PostMapping("/{name}/backlog")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Iterable<GameDto>> addGame(@PathVariable String name, @RequestParam Long gameId) {
        var maybeClub = clubs.getClubById(name);
        var maybeGame = games.findById(gameId);
        if (maybeClub.isPresent() && maybeGame.isPresent()) {
            var club = maybeClub.get();
            clubs.addGameToBacklog(club, maybeGame.get());
            return ResponseEntity.status(HttpStatus.OK).body(clubs.getBacklog(club));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{name}/backlog")
    @ResponseStatus(HttpStatus.OK)
    public void removeGame(@PathVariable String name, @RequestParam Long gameId) {
        clubs.removeGameFromBacklog(name, gameId);
    }

    @GetMapping("/{name}/gamesEveryone")
    public Iterable<GameDto> getGamesOwnedByEveryone(@PathVariable String name) {
       var club = clubs.getClubById(name).orElseThrow();
       return clubs.getGamesOwnedByAll(club);
    }
}
