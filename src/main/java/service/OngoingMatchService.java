package service;

import dao.PlayerDao;
import dto.PlayersPair;
import dto.view.CurrentMatchDto;
import dto.view.MatchOverDto;
import dto.view.PlayerDto;
import entity.Player;

import matches.CurrentMatch;
import model.Score;
import storages.CurrentMatchStorage;
import transaction.TransactionManager;

import java.util.Optional;
import java.util.UUID;

public class OngoingMatchService {
    private final CompletedMatchService completedMatchService;
    private final CurrentMatchStorage currentMatchStorage;
    private final PlayerDao playerDao;
    private final TransactionManager transactionManager;

    public OngoingMatchService(PlayerDao playerDao, CurrentMatchStorage currentMatchStorage, CompletedMatchService completedMatchService, TransactionManager transactionManager) {
        this.currentMatchStorage = currentMatchStorage;
        this.playerDao = playerDao;
        this.completedMatchService = completedMatchService;
        this.transactionManager = transactionManager;
    }
    private OngoingMatchContext loadOngoingMatchContext(UUID matchId){
        CurrentMatch currentMatch = currentMatchStorage.getMap().get(matchId);

        Long firstPlayerId = currentMatch.getFirstPlayerId();
        Long secondPlayerId = currentMatch.getSecondPlayerId();

        PlayersPair playersPair = transactionManager.executeInTransaction(() -> {
                    Player firstPlayer = playerDao.findByID(firstPlayerId);
                    Player secondPlayer = playerDao.findByID(secondPlayerId);
                    return new PlayersPair(firstPlayer, secondPlayer);
                }
                );

        return new OngoingMatchContext(currentMatch, matchId, playersPair.firstPlayer(), playersPair.secondPlayer());
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
        if (currentPlayerScore.isTiebreak() && opponentPlayerScore.isTiebreak()){
            addTiebreakPoint(currentPlayerScore, opponentPlayerScore);
        }
        else {
            addRegularGamePoint(currentPlayerScore, opponentPlayerScore);
        }
    }
    public void checkCountGames(Score currentPlayerScore, Score opponentPlayerScore){
        if (shouldStartTiebreak(currentPlayerScore, opponentPlayerScore)){
            currentPlayerScore.setTiebreak(true);
            opponentPlayerScore.setTiebreak(true);
        }
        else if (isSetWon(currentPlayerScore, opponentPlayerScore)){
            currentPlayerScore.setGames(0);
            opponentPlayerScore.setGames(0);
            currentPlayerScore.addSets(1);
        }

    }
    private void addTiebreakPoint(Score currentPlayerScore, Score opponentPlayerScore){
        String points = currentPlayerScore.getPoints();
        String opponentPoints = opponentPlayerScore.getPoints();

        int numberPoints = Integer.parseInt(points) + 1;
        currentPlayerScore.setPoints(String.valueOf(numberPoints));

        int numberOpponentPoints = Integer.parseInt(opponentPoints);

        if ((numberPoints - numberOpponentPoints >= 2) &&
                (numberPoints >= 7)){
            currentPlayerScore.setTiebreak(false);
            opponentPlayerScore.setTiebreak(false);
            currentPlayerScore.setGames(0);
            opponentPlayerScore.setGames(0);
            currentPlayerScore.setPoints("0");
            opponentPlayerScore.setPoints("0");
            currentPlayerScore.addSets(1);
        }


    }

    private  void addRegularGamePoint(Score currentPlayerScore, Score opponentPlayerScore){
        String points = currentPlayerScore.getPoints();
        String opponentPoints = opponentPlayerScore.getPoints();

        if (points.equals("0")) {
            currentPlayerScore.setPoints("15");
        }
        else if (points.equals("15")) {
            currentPlayerScore.setPoints("30");
        }
        else if (points.equals("30")) {
            currentPlayerScore.setPoints("40");
        }
        else if (points.equals("40") && (!opponentPoints.equals("40") && !opponentPoints.equals("AD"))) {
            currentPlayerScore.setPoints("0");
            opponentPlayerScore.setPoints("0");
            currentPlayerScore.addGames(1);
        }
        else if (points.equals("40") && opponentPoints.equals("AD")) {
            currentPlayerScore.setPoints("40");
            opponentPlayerScore.setPoints("40");
        }
        else if (points.equals("40") && opponentPoints.equals("40")) {
            currentPlayerScore.setPoints("AD");
        }
        else if (points.equals("AD") && opponentPoints.equals("40")) {
            currentPlayerScore.addGames(1);
            currentPlayerScore.setPoints("0");
            opponentPlayerScore.setPoints("0");
        }
        else if (points.equals("AD") && opponentPoints.equals("AD")) {
            currentPlayerScore.setPoints("40");
            opponentPlayerScore.setPoints("40");
        }
    }
    public boolean shouldStartTiebreak(Score currentPlayerScore, Score opponentPlayerScore){
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

    public CurrentMatchDto getMatchView(String uuid) {
        UUID matchId = UUID.fromString(uuid);

        OngoingMatchContext matchContext = loadOngoingMatchContext(matchId);

        PlayerDto firstPlayerDto = new PlayerDto(
                matchContext.getFirstPlayerId(),
                matchContext.getFirstPlayer().getName()
        );
        PlayerDto secondPlayerDto = new PlayerDto(
                matchContext.getSecondPlayerId(),
                matchContext.getSecondPlayer().getName()
        );
        return new CurrentMatchDto(
                matchId,
                firstPlayerDto,
                secondPlayerDto,
                matchContext.getFirstPlayerScore(),
                matchContext.getSecondPlayerScore()
        );
    }

    public Optional<MatchOverDto> getMatchOverView(String uuid){
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
