package service;

import entity.Player;
import lombok.Getter;
import matches.CurrentMatch;
import scores.Score;

import java.util.UUID;

@Getter
public class OngoingMatchContext{
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
