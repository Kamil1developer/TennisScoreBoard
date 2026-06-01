import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Score;
import service.CompletedMatchService;
import service.OngoingMatchService;
import storages.CurrentMatchStorage;
import transaction.TransactionManager;

import static org.junit.jupiter.api.Assertions.*;

public class OngoingMatchServiceTest {
    OngoingMatchService ongoingMatchService;

    @BeforeEach
    void setUp(){
        FakePlayerDao fakePlayerDao = new FakePlayerDao();
        FakeMatchDao fakeMatchDao = new FakeMatchDao();
        CurrentMatchStorage currentMatchStorage = new CurrentMatchStorage();
        TransactionManager transactionManager = new FakeTransaction();


        CompletedMatchService completedMatchService = new CompletedMatchService(fakeMatchDao, fakePlayerDao, currentMatchStorage, transactionManager );

        this.ongoingMatchService = new OngoingMatchService(fakePlayerDao,currentMatchStorage, completedMatchService, transactionManager);
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

    @Test
    void shouldStartTiebreakWhenSetScoreIsSixSix(){
        Score currentPlayerScore = new Score(0,6,"0");
        Score opponentPlayerScore = new Score(0,6,"0");

        ongoingMatchService.checkCountGames(currentPlayerScore, opponentPlayerScore);
        ongoingMatchService.addPoints(currentPlayerScore, opponentPlayerScore);

        assertEquals("1", currentPlayerScore.getPoints());
    }

    @Test
    void shouldWinGameWhenPlayerWinsPointAtFortyThirty(){
        Score currentPlayerScore = new Score(0,0,"40");
        Score opponentPlayerScore = new Score(0,0,"30");

        ongoingMatchService.addPoints(currentPlayerScore, opponentPlayerScore);

        assertEquals(1, currentPlayerScore.getGames());
    }

    @Test
    void shouldNotFinishGameWhenPlayerWinsPointAtDeuce(){
        Score currentPlayerScore = new Score(0,0,"40");
        Score opponentPlayerScore = new Score(0,0,"40");

        ongoingMatchService.addPoints(currentPlayerScore, opponentPlayerScore);

        assertEquals(currentPlayerScore.getGames(), opponentPlayerScore.getGames());
    }

    @Test
    void shouldResetPointsAfterGameIsWon(){
        Score currentPlayerScore = new Score(0,0,"40");
        Score opponentPlayerScore = new Score(0,0,"30");

        ongoingMatchService.addPoints(currentPlayerScore, opponentPlayerScore);


        assertAll(
                () -> assertEquals("0", opponentPlayerScore.getPoints()),
                () -> assertEquals("0", currentPlayerScore.getPoints())
        );

    }


}
