package service;

import matches.CurrentMatch;
import scores.Scores;
import storages.CurrentMatchStorage;

import java.util.UUID;

public class MatchScoreService {
    private final  CurrentMatchStorage currentMatchStorage;

    public MatchScoreService(CurrentMatchStorage currentMatchStorage) {
        this.currentMatchStorage = currentMatchStorage;
    }

    public CurrentMatchStorage getCurrentMatchStorage() {
        return currentMatchStorage;
    }

    public void addScore(String uuid, String playerId){
        CurrentMatch currentMatch = currentMatchStorage.getMap().get(UUID.fromString(uuid));

        if (Long.getLong(playerId).equals(currentMatch.firstPlayerId())) {
            Scores firstPlayerScores = currentMatch.firstPlayerScores();
        }

        if (Long.getLong(playerId).equals(currentMatch.firstPlayerId())) {
            Scores secondPlayerScores = currentMatch.secondPlayerScores();
        }
    }

    private void addPoints(Scores playerScores){
        if (playerScores.getPoints() < 30){
        }
    }
}
