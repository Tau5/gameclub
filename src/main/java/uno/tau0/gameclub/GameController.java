package uno.tau0.gameclub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uno.tau0.gameclub.dto.GameDto;
import uno.tau0.gameclub.entity.Game;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameRepository repository;

    @GetMapping
    public Iterable<GameDto> findAll() {
        return repository.findAll().stream().map(GameDto::new).toList();
    }

    record NewGame(
            String title,
            Integer year
    ) { }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Game create(@RequestBody NewGame newGame) {
        return repository.save(new Game(newGame.title, newGame.year));
    }
}
