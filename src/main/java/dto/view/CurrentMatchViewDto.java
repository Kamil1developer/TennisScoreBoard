package dto.view;

import scores.Score;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CurrentMatchViewDto {
    private final PlayerViewDto firstPlayerDto;
    private final PlayerViewDto secondPlayerDto;
    private final Score firstPlayerScores;
    private final Score secondPlayerScores;
}
