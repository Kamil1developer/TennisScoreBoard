package service;

import dao.PlayerDao;
import dto.view.CurrentMatchViewDto;
import dto.view.MatchOverViewDto;
import dto.view.PlayerViewDto;
import entity.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
    @Getter
    private static class OngoingMatchContext{
        private final UUID matchId;
        private final CurrentMatch currentMatch;

        private final Long firstPlayerId;
        private final Long secondPlayerId;

        private final Player firstPlayer;
        private final Player secondPlayer;

        private final Score firstPlayerScore;
        private final Score secondPlayerScore;

        public OngoingMatchContext(CurrentMatch currentMatch, UUID matchId, Player firstPlayer, Player secondPlayer){
            this.matchId = matchId;
            this.currentMatch = currentMatch;

            this.firstPlayerId = currentMatch.getFirstPlayerId();
            this.secondPlayerId = currentMatch.getSecondPlayerId();

            this.firstPlayer = firstPlayer;
            this.secondPlayer = secondPlayer;

            this.firstPlayerScore = currentMatch.getFirstPlayerScore();
            this.secondPlayerScore = currentMatch.getSecondPlayerScore();
        }

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

            checkCountGames(currentPlayerScore, opponentPlayerScore);
        }

        if (matchContext.getSecondPlayerId() == Long.parseLong(playerId)) {
            Score currentPlayerScore = matchContext.getSecondPlayerScore();
            Score opponentPlayerScore = matchContext.getFirstPlayerScore();

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
            currentPlayerScore.setGames(0);
            opponentPlayerScore.setGames(0);
            currentPlayerScore.addSets(1);
        }

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
                matchContext.firstPlayer.getName()
        );
        PlayerViewDto secondPlayerDto = new PlayerViewDto(
                matchContext.secondPlayerId,
                matchContext.secondPlayer.getName()
        );
        return new CurrentMatchViewDto(
                matchId,
                firstPlayerDto,
                secondPlayerDto,
                matchContext.firstPlayerScore,
                matchContext.secondPlayerScore
        );
    }

    public Optional<MatchOverViewDto> getMatchOverView(String uuid){
        UUID matchId = UUID.fromString(uuid);

        OngoingMatchContext matchContext = loadOngoingMatchContext(matchId);
        CurrentMatch currentMatch = currentMatchStorage.getMap().get(matchId);

        int firstPlayerSets = currentMatch.getFirstPlayerScore().getSets();
        int secondPlayerSets = currentMatch.getSecondPlayerScore().getSets();


        if (firstPlayerSets > secondPlayerSets){
            String winnerName = matchContext.firstPlayer.getName();
            int winnerSets = matchContext.getFirstPlayerScore().getSets();
            String loserName = matchContext.secondPlayer.getName();
            int loserSets = currentMatch.getSecondPlayerScore().getSets();
            Long winnerId = matchContext.firstPlayerId;

            completedMatchService.safe(
                    matchContext.firstPlayerId,
                    matchContext.secondPlayerId,
                    winnerId
            );

            return Optional.of(new MatchOverViewDto(winnerName,winnerSets,loserName,loserSets));
        }
        if (firstPlayerSets < secondPlayerSets){
            String winnerName = matchContext.secondPlayer.getName();
            int winnerSets = currentMatch.getSecondPlayerScore().getSets();
            String loserName = matchContext.firstPlayer.getName();
            int loserSets = currentMatch.getFirstPlayerScore().getSets();
            Long winnerId = matchContext.secondPlayerId;


            completedMatchService.safe(
                    matchContext.firstPlayerId,
                    matchContext.secondPlayerId,
                    winnerId
            );

            return Optional.of(new MatchOverViewDto(winnerName,winnerSets,loserName,loserSets));
        }

        return Optional.empty();
    }





}
