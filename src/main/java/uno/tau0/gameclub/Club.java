package uno.tau0.gameclub;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@Data
public class Club {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    @Nullable
    Game currentGame;

    @ManyToMany
    @JoinTable(name = "club_games")
    Set<Game> backlog;

    String name;

    @ManyToOne
    Group group;

    public Club(String name, Group group) {
        this.name = name;
        this.group = group;
    }
}
