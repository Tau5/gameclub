package uno.tau0.gameclub.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@RequiredArgsConstructor
@Data
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer year;

    @ManyToMany(mappedBy = "ownedGames")
    public List<User> owners;

    @ManyToMany(mappedBy = "backlog")
    public List<Club> clubs;

    public Game(String title, Integer year) {
        this.title = title;
        this.year = year;
    }
}
