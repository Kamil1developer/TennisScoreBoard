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

            checkCountGames(currentPlayerScore, opponentPlayerScore);
        }

        if (secondPlayerId == Long.parseLong(playerId)) {
            Score currentPlayerScore = currentMatch.getSecondPlayerScore();
            Score opponentPlayerScore = currentMatch.getFirstPlayerScore();

            checkCountGames(currentPlayerScore, opponentPlayerScore);
        }
    }

    private void addPoints(Score currentPlayerScore, Score opponentPlayerScore){
        String points = currentPlayerScore.getPoints();
        String opponentPoints = opponentPlayerScore.getPoints();

        if (points.equals("0")){
            currentPlayerScore.setPoints("15");
        }
        else if (points.equals("15")){
            currentPlayerScore.setPoints("30");
        }
        else if (points.equals("30")){
            currentPlayerScore.setPoints("40");
        }
        else if (points.equals("40") && (!opponentPoints.equals("40") && !opponentPoints.equals("AD"))){
            currentPlayerScore.setPoints("0");
            opponentPlayerScore.setPoints("0");
            currentPlayerScore.addGames(1);
        }
        else if (points.equals("40") && opponentPoints.equals("AD")){
            currentPlayerScore.setPoints("40");
            opponentPlayerScore.setPoints("40");
        }
        else if (points.equals("40") && opponentPoints.equals("40")){
            currentPlayerScore.setPoints("AD");
        }
        else if (points.equals("AD") && opponentPoints.equals("40")){
            currentPlayerScore.addGames(1);
            currentPlayerScore.setPoints("0");
            opponentPlayerScore.setPoints("0");
        }
        else if (points.equals("AD") && opponentPoints.equals("AD")){
            currentPlayerScore.setPoints("40");
            opponentPlayerScore.setPoints("40");
        }
    }
    private void checkCountGames(Score currentPlayerScore, Score opponentPlayerScore){

        addPoints(currentPlayerScore, opponentPlayerScore);
        if (isSetWon(currentPlayerScore, opponentPlayerScore)){
            currentPlayerScore.addSets(1);
        }

        checkCountSets(currentPlayerScore, opponentPlayerScore);

    }
    private boolean isSetWon(Score currentPlayerScore, Score opponentPlayerScore){
        return (currentPlayerScore.getGames() == 6 && opponentPlayerScore.getGames() < 5 ||
                currentPlayerScore.getGames() == 7 && opponentPlayerScore.getGames() == 5);
    }

    public void checkCountSets(Score currentPlayerScore, Score opponentPlayerScore){
        if (currentPlayerScore.getSets() == 2 || opponentPlayerScore.getSets() == 2){

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
        return new CurrentMatchViewDto(
                matchId,
                firstPlayerDto,
                secondPlayerDto,
                firstPlayerScore,
                secondPlayerScore
        );
    }




}
