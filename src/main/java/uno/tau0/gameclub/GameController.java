package uno.tau0.gameclub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameRepository repository;

    @GetMapping
    public Iterable<Game> findAll() {
        return repository.findAll();
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
