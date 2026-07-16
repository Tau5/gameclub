package uno.tau0.gameclub.dto;

public record RegisterRequest(
        String username,
        String displayName,
        String password
) { }
