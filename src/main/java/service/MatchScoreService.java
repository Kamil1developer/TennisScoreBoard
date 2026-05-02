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


        Long firstPlayerId = currentMatch.firstPlayerId();
        Long secondPlayerId = currentMatch.secondPlayerId();
        Scores firstPlayerScores = currentMatch.firstPlayerScores();
        Scores secondPlayerScores = currentMatch.secondPlayerScores();

        if (firstPlayerId == Long.parseLong(playerId)) {
            addPoints(firstPlayerScores, secondPlayerScores);
        }

        if (secondPlayerId == Long.parseLong(playerId)) {
            addPoints(secondPlayerScores, firstPlayerScores);
        }
    }

    private void addPoints(Scores currentPlayerScore,Scores opponentPlayerScore ){
        int points = currentPlayerScore.getPoints();
        int opponentPoints = opponentPlayerScore.getPoints();
        boolean advantage = currentPlayerScore.isAd();
        boolean opponentAdvantage = opponentPlayerScore.isAd();

        if (points < 15){
            currentPlayerScore.setPoints(15);
        }
        else if (15 < points && points < 30){
            currentPlayerScore.setPoints(30);
        }
        else if (points == 40 && opponentPoints < 40){
            currentPlayerScore.setPoints(0);
            currentPlayerScore.addGames(1);
        }
        else if (points == 40 && opponentPoints == 40){
            currentPlayerScore.setAd(true);
        }
        else if (advantage && opponentPoints == 40){
            currentPlayerScore.addGames(1);
        }
        else if (advantage && opponentAdvantage){
            currentPlayerScore.setAd(false);
            opponentPlayerScore.setAd(false);
        }
    }
}
