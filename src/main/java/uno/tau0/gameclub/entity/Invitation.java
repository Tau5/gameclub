package uno.tau0.gameclub.entity;

import jakarta.persistence.*;
import lombok.*;
import uno.tau0.gameclub.UserRepository;

import java.time.Instant;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NonNull
    @ManyToOne
    User generatedBy;

    @NonNull
    Instant expiry;

    @NonNull
    Integer usesLeft;

    public boolean isValid() {
        return expiry.isAfter(Instant.now()) && usesLeft > 0;
    }
}
