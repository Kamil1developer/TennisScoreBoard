import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scores.Score;
import service.CompletedMatchService;
import service.OngoingMatchService;
import storages.CurrentMatchStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OngoingMatchServiceTest {
    OngoingMatchService ongoingMatchService;

    @BeforeEach
    void setUp(){
        FakePlayerDao fakePlayerDao = new FakePlayerDao();
        FakeMatchDao fakeMatchDao = new FakeMatchDao();
        CurrentMatchStorage currentMatchStorage = new CurrentMatchStorage();
        CompletedMatchService completedMatchService = new CompletedMatchService(fakeMatchDao, fakePlayerDao, currentMatchStorage);

        this.ongoingMatchService = new OngoingMatchService(fakePlayerDao,currentMatchStorage, completedMatchService);
    }

    @Test
    void shouldNotFinishGameWhenFirstPlayerWinsPointAtDeuce(){
        Score currentPlayerScore = new Score(0,0,"40");
        Score opponentPlayerScore = new Score(0,0,"40");

        int currentPlayerGamesBeforePoint = currentPlayerScore.getGames();

        ongoingMatchService.addPoints(currentPlayerScore, opponentPlayerScore);

        int currentPlayerGamesAfterPoint = currentPlayerScore.getGames();

        assertEquals(currentPlayerGamesBeforePoint, currentPlayerGamesAfterPoint);
    }

    @Test
    void shouldWinGameWhenFirstPlayerWinsPointAtFortyZero(){
        Score currentPlayerScore = new Score(0,0,"40");
        Score opponentPlayerScore = new Score(0,0,"0");

        int currentPlayerGamesBeforePoint = currentPlayerScore.getGames();

        ongoingMatchService.addPoints(currentPlayerScore, opponentPlayerScore);

        int currentPlayerGamesAfterPoint = currentPlayerScore.getGames();

        assertTrue(currentPlayerGamesBeforePoint < currentPlayerGamesAfterPoint);
    }
}
