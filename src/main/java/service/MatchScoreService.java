package service;

import dao.PlayerDao;
import dto.view.CurrentMatchViewDto;
import entity.Player;
import lombok.Getter;
import matches.CurrentMatch;
import scores.Scores;
import storages.CurrentMatchStorage;

import java.util.UUID;

public class MatchScoreService {
    @Getter
    private final CurrentMatchStorage currentMatchStorage;
    private final PlayerDao playerDao;

    public MatchScoreService(PlayerDao playerDao, CurrentMatchStorage currentMatchStorage) {
        this.currentMatchStorage = currentMatchStorage;
        this.playerDao = playerDao;
    }

    public void addScore(String uuid, String playerId){
        UUID matchId = UUID.fromString(uuid);
        CurrentMatch currentMatch = currentMatchStorage.getMap().get(matchId);


        Long firstPlayerId = currentMatch.getFirstPlayerId();
        Long secondPlayerId = currentMatch.getSecondPlayerId();


        if (firstPlayerId == Long.parseLong(playerId)) {
            addPoints(currentMatch);
        }

        if (secondPlayerId == Long.parseLong(playerId)) {
            addPoints(currentMatch);
        }
    }

    private void addPoints(CurrentMatch currentMatch){
        Scores currentPlayerScore = currentMatch.getFirstPlayerScores();
        Scores opponentPlayerScore = currentMatch.getSecondPlayerScores();

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

    public CurrentMatchViewDto getCurrentMatchView(String uuid) {
        UUID matchId = UUID.fromString(uuid);
        CurrentMatchStorage matchStorage = getCurrentMatchStorage();
        CurrentMatch currentMatch = matchStorage.getMap().get(matchId);

        Long firstPlayerId = currentMatch.getFirstPlayerId();
        Long secondPlayerId = currentMatch.getSecondPlayerId();

        Player firstPlayer = playerDao.findByID(firstPlayerId);
        Player secondPlayer = playerDao.findByID(secondPlayerId);

        Scores firstPlayerScores = currentMatch.getFirstPlayerScores();
        Scores secondPlayerScores = currentMatch.getSecondPlayerScores();

        return new CurrentMatchViewDto(
                firstPlayer.getName(),
                secondPlayer.getName(),
                firstPlayerScores,
                secondPlayerScores
        );
    }


}
