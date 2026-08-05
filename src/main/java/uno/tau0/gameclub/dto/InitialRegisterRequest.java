package uno.tau0.gameclub.dto;

public record InitialRegisterRequest(
        String username,
        String displayName,
        String password
) { }
