package dto.view;

import scores.Scores;

public record CurrentMatchViewDto(
        String firstPlayerName,
        String secondPlayerName,
        Scores firstPlayerScores,
        Scores secondPlayerScores
        ) {}
