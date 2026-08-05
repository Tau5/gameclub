package uno.tau0.gameclub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;
import java.util.UUID;

@Entity
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    Instant expiry;

    User generatedBy;

    Integer usesLeft;
}
