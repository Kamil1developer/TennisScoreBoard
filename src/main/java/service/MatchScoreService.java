package service;

import dao.PlayerDao;
import dto.view.CurrentMatchViewDto;
import dto.view.PlayerViewDto;
import entity.Player;
import lombok.Getter;
import matches.CurrentMatch;
import scores.Score;
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
            Score currentPlayerScore = currentMatch.getFirstPlayerScore();
            Score opponentPlayerScore = currentMatch.getSecondPlayerScore();

            addPoints(currentPlayerScore, opponentPlayerScore);
        }

        if (secondPlayerId == Long.parseLong(playerId)) {
            Score currentPlayerScore = currentMatch.getSecondPlayerScore();
            Score opponentPlayerScore = currentMatch.getFirstPlayerScore();

            addPoints(currentPlayerScore, opponentPlayerScore);
        }
    }

    private void addPoints(Score currentPlayerScore, Score opponentPlayerScore){

        int points = currentPlayerScore.getPoints();
        int opponentPoints = opponentPlayerScore.getPoints();
        boolean advantage = currentPlayerScore.isAd();
        boolean opponentAdvantage = opponentPlayerScore.isAd();

        if (points < 15){
            currentPlayerScore.setPoints(15);
        }
        else if (points == 15){
            currentPlayerScore.setPoints(30);
        }
        else if (points == 30){
            currentPlayerScore.setPoints(40);
        }
        else if (points == 40 && opponentPoints < 40){
            currentPlayerScore.setPoints(0);
            currentPlayerScore.addGames(1);
        }
        else if (advantage && opponentPoints == 40){
            currentPlayerScore.addGames(1);
        }
        else if (advantage && opponentAdvantage){
            currentPlayerScore.setAd(false);
            opponentPlayerScore.setAd(false);
        }
        else if (points == 40 && opponentPoints == 40){
            currentPlayerScore.setAd(true);
        }
    }

    public CurrentMatchViewDto getMatchView(String uuid) {
        UUID matchId = UUID.fromString(uuid);
        CurrentMatch currentMatch = currentMatchStorage.getMap().get(matchId);

        Long firstPlayerId = currentMatch.getFirstPlayerId();
        Long secondPlayerId = currentMatch.getSecondPlayerId();

        Player firstPlayer = playerDao.findByID(firstPlayerId);
        Player secondPlayer = playerDao.findByID(secondPlayerId);

        Score firstPlayerScore = currentMatch.getFirstPlayerScore();
        Score secondPlayerScore = currentMatch.getSecondPlayerScore();

        PlayerViewDto firstPlayerDto = new PlayerViewDto(firstPlayerId, firstPlayer.getName());
        PlayerViewDto secondPlayerDto = new PlayerViewDto(secondPlayerId, secondPlayer.getName());
        return new CurrentMatchViewDto(matchId,
                firstPlayerDto,
                secondPlayerDto,
                firstPlayerScore,
                secondPlayerScore
        );
    }




}
