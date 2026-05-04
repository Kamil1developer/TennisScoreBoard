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
        CurrentMatch currentMatch = currentMatchStorage.getMap().get(UUID.fromString(uuid));


        Long firstPlayerId = currentMatch.firstPlayerId();
        Long secondPlayerId = currentMatch.secondPlayerId();


        if (firstPlayerId == Long.parseLong(playerId)) {
            addPoints(currentMatch);
        }

        if (secondPlayerId == Long.parseLong(playerId)) {
            addPoints(currentMatch);
        }
    }

    private void addPoints(CurrentMatch currentMatch){
        Scores currentPlayerScore = currentMatch.firstPlayerScores();
        Scores opponentPlayerScore = currentMatch.secondPlayerScores();

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

    public CurrentMatchViewDto getCurrentMatchView(Long firstPlayerId, Long secondPlayerId, String uuid){
        UUID matchId = UUID.fromString(uuid);
        CurrentMatch currentMatch = currentMatchStorage.getMap().get(matchId);

        Player firstPlayer = playerDao.findByID(firstPlayerId);
        Player secondPlayer = playerDao.findByID(secondPlayerId);

        Scores firstPlayerScores = currentMatch.firstPlayerScores();
        Scores secondPlayerScores = currentMatch.secondPlayerScores();

        return new CurrentMatchViewDto(
                firstPlayer.getName(),
                secondPlayer.getName(),
                firstPlayerScores,
                secondPlayerScores
        );


    }


}
