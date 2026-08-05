package uno.tau0.gameclub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
@Data
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NonNull
    User generatedBy;

    @NonNull
    Instant expiry;

    @NonNull
    Integer usesLeft;

    public boolean isValid() {
        return expiry.isAfter(Instant.now()) && usesLeft > 0;
    }

}
