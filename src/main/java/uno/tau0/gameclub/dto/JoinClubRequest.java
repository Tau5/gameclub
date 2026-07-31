package uno.tau0.gameclub.dto;

public record JoinClubRequest (
    String clubName,
    String password
) { }