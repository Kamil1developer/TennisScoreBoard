package service;

import dao.MatchDao;
import dao.PlayerDao;
import dto.view.MatchOverViewDto;
import entity.Match;
import entity.Player;
import matches.CurrentMatch;
import storages.CurrentMatchStorage;

import java.util.Optional;
import java.util.UUID;

public class CompletedMatchService {

    private final MatchDao matchDao;
    private final PlayerDao playerDao;
    private final CurrentMatchStorage currentMatchStorage;
    public CompletedMatchService(MatchDao matchDao, PlayerDao playerDao, CurrentMatchStorage currentMatchStorage) {
        this.matchDao = matchDao;
        this.playerDao = playerDao;
        this.currentMatchStorage = currentMatchStorage;
    }

    public void safe(Player firstPlayer, Player secondPlayer, Player winner){
        Match match = new Match(firstPlayer, secondPlayer, winner);
        matchDao.insert(match);
    }

    private OngoingMatchContext loadOngoingMatchContext(UUID matchId){
        CurrentMatch currentMatch = currentMatchStorage.getMap().get(matchId);

        Long firstPlayerId = currentMatch.getFirstPlayerId();
        Long secondPlayerId = currentMatch.getSecondPlayerId();

        Player firstPlayer = playerDao.findByID(firstPlayerId);
        Player secondPlayer = playerDao.findByID(secondPlayerId);

        return new OngoingMatchContext(currentMatch, matchId, firstPlayer, secondPlayer);
    }

    public Optional<MatchOverViewDto> safeCompletedMatch(int firstPlayerSets, int secondPlayerSets, UUID matchId) {
        OngoingMatchContext matchContext = loadOngoingMatchContext(matchId);
        CurrentMatch currentMatch = currentMatchStorage.getMap().get(matchId);
        if (firstPlayerSets > secondPlayerSets) {
            String winnerName = matchContext.getFirstPlayer().getName();
            int winnerSets = matchContext.getFirstPlayerScore().getSets();
            String loserName = matchContext.getSecondPlayer().getName();
            int loserSets = currentMatch.getSecondPlayerScore().getSets();

            Player firstPlayer = playerDao.findByID(matchContext.getFirstPlayerId());
            Player secondPlayer = playerDao.findByID(matchContext.getSecondPlayerId());
            Player winner = playerDao.findByID(matchContext.getFirstPlayerId());

            safe(firstPlayer, secondPlayer, winner);
            currentMatchStorage.remove(matchId);
            return Optional.of(new MatchOverViewDto(winnerName, winnerSets, loserName, loserSets));
        }
        if (firstPlayerSets < secondPlayerSets) {
            String winnerName = matchContext.getSecondPlayer().getName();
            int winnerSets = currentMatch.getSecondPlayerScore().getSets();
            String loserName = matchContext.getFirstPlayer().getName();
            int loserSets = currentMatch.getFirstPlayerScore().getSets();

            Player firstPlayer = playerDao.findByID(matchContext.getFirstPlayerId());
            Player secondPlayer = playerDao.findByID(matchContext.getSecondPlayerId());
            Player winner = playerDao.findByID(matchContext.getSecondPlayerId());

            safe(firstPlayer, secondPlayer, winner);
            currentMatchStorage.remove(matchId);
            return Optional.of(new MatchOverViewDto(winnerName, winnerSets, loserName, loserSets));
        }

        return Optional.empty();
    }



}
