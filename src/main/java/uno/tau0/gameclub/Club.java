package uno.tau0.gameclub;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
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

    String password;

    @ManyToMany(mappedBy = "clubs")
    List<User> members;

    public Club(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
