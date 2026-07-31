package uno.tau0.gameclub.dto;

import uno.tau0.gameclub.Game;

public record GameDto(
        Long id,
        String title,
        Integer year
) {
    public GameDto(Game game) {
        this(game.getId(), game.getTitle(), game.getYear());
    }
}
