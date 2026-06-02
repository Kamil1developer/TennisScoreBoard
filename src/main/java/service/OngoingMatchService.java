package service;

import dao.PlayerDao;
import dto.PlayersPair;
import dto.view.CurrentMatchDto;
import dto.view.MatchOverDto;
import dto.view.PlayerDto;
import entity.Player;

import matches.CurrentMatch;
import model.MatchScore;
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

            MatchScore matchScore = new MatchScore(currentPlayerScore, opponentPlayerScore);

            matchScore.addPoints();
            matchScore.checkCountGames();
        }

        if (matchContext.getSecondPlayerId() == Long.parseLong(playerId)) {
            Score currentPlayerScore = matchContext.getSecondPlayerScore();
            Score opponentPlayerScore = matchContext.getFirstPlayerScore();

            MatchScore matchScore = new MatchScore(currentPlayerScore, opponentPlayerScore);

            matchScore.addPoints();
            matchScore.checkCountGames();
        }


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
