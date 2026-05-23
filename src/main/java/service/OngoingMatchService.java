package service;

import dao.PlayerDao;
import dto.view.CurrentMatchViewDto;
import dto.view.MatchOverViewDto;
import dto.view.PlayerViewDto;
import entity.Player;

import matches.CurrentMatch;
import scores.Score;
import storages.CurrentMatchStorage;

import java.util.Optional;
import java.util.UUID;

public class OngoingMatchService {
    private final CompletedMatchService completedMatchService;
    private final CurrentMatchStorage currentMatchStorage;
    private final PlayerDao playerDao;

    public OngoingMatchService(PlayerDao playerDao, CurrentMatchStorage currentMatchStorage, CompletedMatchService completedMatchService) {
        this.currentMatchStorage = currentMatchStorage;
        this.playerDao = playerDao;
        this.completedMatchService = completedMatchService;
    }
    private OngoingMatchContext loadOngoingMatchContext(UUID matchId){
        CurrentMatch currentMatch = currentMatchStorage.getMap().get(matchId);

        Long firstPlayerId = currentMatch.getFirstPlayerId();
        Long secondPlayerId = currentMatch.getSecondPlayerId();

        Player firstPlayer = playerDao.findByID(firstPlayerId);
        Player secondPlayer = playerDao.findByID(secondPlayerId);

        return new OngoingMatchContext(currentMatch, matchId, firstPlayer, secondPlayer);
    }

    public void addScore(String uuid, String playerId){
        UUID matchId = UUID.fromString(uuid);

        OngoingMatchContext matchContext = loadOngoingMatchContext(matchId);

        if (matchContext.getFirstPlayerId() == Long.parseLong(playerId)) {
            Score currentPlayerScore = matchContext.getFirstPlayerScore();
            Score opponentPlayerScore = matchContext.getSecondPlayerScore();

            addPoints(currentPlayerScore, opponentPlayerScore);
            checkCountGames(currentPlayerScore, opponentPlayerScore);
        }

        if (matchContext.getSecondPlayerId() == Long.parseLong(playerId)) {
            Score currentPlayerScore = matchContext.getSecondPlayerScore();
            Score opponentPlayerScore = matchContext.getFirstPlayerScore();

            addPoints(currentPlayerScore, opponentPlayerScore);
            checkCountGames(currentPlayerScore, opponentPlayerScore);
        }


    }

    public void addPoints(Score currentPlayerScore, Score opponentPlayerScore){
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
    public void checkCountGames(Score currentPlayerScore, Score opponentPlayerScore){
        if (isTiebreak(currentPlayerScore, opponentPlayerScore)){
            currentPlayerScore.setTiebreak(true);
            opponentPlayerScore.setTiebreak(true);
        }
        else if (isSetWon(currentPlayerScore, opponentPlayerScore)){
            currentPlayerScore.setGames(0);
            opponentPlayerScore.setGames(0);
            currentPlayerScore.addSets(1);
        }

    }
    private boolean isTiebreak(Score currentPlayerScore, Score opponentPlayerScore){
        return (currentPlayerScore.getGames() == 6 && opponentPlayerScore.getGames() == 6);
    }
    private boolean isSetWon(Score currentPlayerScore, Score opponentPlayerScore){
        return (currentPlayerScore.getGames() == 6 && opponentPlayerScore.getGames() < 5 ||
                currentPlayerScore.getGames() == 7 && opponentPlayerScore.getGames() == 5);
    }

    public boolean hasMatchWinner(String uuid){
        UUID matchId = UUID.fromString(uuid);

        CurrentMatch currentMatch = currentMatchStorage.getMap().get(matchId);
        Score currentPlayerScore = currentMatch.getFirstPlayerScore();
        Score opponentPlayerScore = currentMatch.getSecondPlayerScore();

        return  (currentPlayerScore.getSets() == 2 || opponentPlayerScore.getSets() == 2);
    }

    public CurrentMatchViewDto getMatchView(String uuid) {
        UUID matchId = UUID.fromString(uuid);

        OngoingMatchContext matchContext = loadOngoingMatchContext(matchId);

        PlayerViewDto firstPlayerDto = new PlayerViewDto(
                matchContext.getFirstPlayerId(),
                matchContext.getFirstPlayer().getName()
        );
        PlayerViewDto secondPlayerDto = new PlayerViewDto(
                matchContext.getSecondPlayerId(),
                matchContext.getSecondPlayer().getName()
        );
        return new CurrentMatchViewDto(
                matchId,
                firstPlayerDto,
                secondPlayerDto,
                matchContext.getFirstPlayerScore(),
                matchContext.getSecondPlayerScore()
        );
    }

    public Optional<MatchOverViewDto> getMatchOverView(String uuid){
        UUID matchId = UUID.fromString(uuid);
        CurrentMatch currentMatch = currentMatchStorage.getMap().get(matchId);

        int firstPlayerSets = currentMatch.getFirstPlayerScore().getSets();
        int secondPlayerSets = currentMatch.getSecondPlayerScore().getSets();
        return completedMatchService.safeCompletedMatch(
                firstPlayerSets,
                secondPlayerSets,
                matchId
        );
    }

}
