package dto.view;

import scores.Score;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CurrentMatchViewDto {
    private final UUID uuid;
    private final PlayerViewDto firstPlayerDto;
    private final PlayerViewDto secondPlayerDto;
    private final Score firstPlayerScore;
    private final Score secondPlayerScore;
}
