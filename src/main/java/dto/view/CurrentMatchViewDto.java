package dto.view;

import scores.Scores;

import lombok.AllArgsConstructor;
import lombok.Getter;
import scores.Scores;

@AllArgsConstructor
@Getter
public class CurrentMatchViewDto {
    private final String firstPlayerName;
    private final String secondPlayerName;
    private final Scores firstPlayerScores;
    private final Scores secondPlayerScores;
}

