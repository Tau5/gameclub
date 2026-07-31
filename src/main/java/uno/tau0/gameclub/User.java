package uno.tau0.gameclub;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails {
    @Id
    public String name;

    public String displayName;

    public String role;

    public String password;

    @ManyToMany
    @JoinTable(name="game_ownership")
    public Set<Game> ownedGames;

    @ManyToMany
    @JoinTable(name="user_groups")
    public Set<Group> groups;

    public User(String name, String displayName, String password, Set<Group> groups) {
        this.name = name;
        this.displayName = displayName;
        this.password = password;
        this.role = "USER";
        this.groups = groups;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(role)
        );
    }

    @Override
    public String getUsername() {
        return name;
    }
}
