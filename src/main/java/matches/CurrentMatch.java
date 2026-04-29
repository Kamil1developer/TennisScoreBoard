package matches;


import scores.Scores;

public record CurrentMatch(Long firstPlayerId, Long secondPlayerId, Scores firstPlayerScores, Scores secondPlayerScores){ }
