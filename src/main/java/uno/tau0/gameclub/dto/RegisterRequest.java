package uno.tau0.gameclub.dto;

import java.util.Optional;
import java.util.UUID;

public record RegisterRequest(
        String username,
        String displayName,
        String password,
        Optional<UUID> invitationCode
) { }

