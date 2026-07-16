package uno.tau0.gameclub;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity(name = "clubGroup")
@RequiredArgsConstructor
@Data
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @OneToMany
    List<Club> clubs;

    String name;

    @OneToMany
    List<User> members;

    public Group(String name) {
        this.name = name;
    }
}
