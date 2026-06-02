import model.MatchScore;
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

        MatchScore matchScore = new MatchScore(currentPlayerScore, opponentPlayerScore);

        matchScore.addPoints();

        int currentPlayerGamesAfterPoint = currentPlayerScore.getGames();

        assertEquals(currentPlayerGamesBeforePoint, currentPlayerGamesAfterPoint);
    }

    @Test
    void shouldWinGameWhenFirstPlayerWinsPointAtFortyZero(){
        Score currentPlayerScore = new Score(0,0,"40");
        Score opponentPlayerScore = new Score(0,0,"0");

        int currentPlayerGamesBeforePoint = currentPlayerScore.getGames();

        MatchScore matchScore = new MatchScore(currentPlayerScore, opponentPlayerScore);

        matchScore.addPoints();

        int currentPlayerGamesAfterPoint = currentPlayerScore.getGames();

        assertTrue(currentPlayerGamesBeforePoint < currentPlayerGamesAfterPoint);
    }

    @Test
    void shouldStartTiebreakWhenSetScoreIsSixSix(){
        Score currentPlayerScore = new Score(0,6,"0");
        Score opponentPlayerScore = new Score(0,6,"0");

        MatchScore matchScore = new MatchScore(currentPlayerScore, opponentPlayerScore);

        matchScore.checkCountGames();
        matchScore.addPoints();

        assertEquals("1", currentPlayerScore.getPoints());
    }

    @Test
    void shouldWinGameWhenPlayerWinsPointAtFortyThirty(){
        Score currentPlayerScore = new Score(0,0,"40");
        Score opponentPlayerScore = new Score(0,0,"30");

        MatchScore matchScore = new MatchScore(currentPlayerScore, opponentPlayerScore);

        matchScore.addPoints();

        assertEquals(1, currentPlayerScore.getGames());
    }

    @Test
    void shouldNotFinishGameWhenPlayerWinsPointAtDeuce(){
        Score currentPlayerScore = new Score(0,0,"40");
        Score opponentPlayerScore = new Score(0,0,"40");

        MatchScore matchScore = new MatchScore(currentPlayerScore, opponentPlayerScore);

        matchScore.addPoints();

        assertEquals(currentPlayerScore.getGames(), opponentPlayerScore.getGames());
    }

    @Test
    void shouldResetPointsAfterGameIsWon(){
        Score currentPlayerScore = new Score(0,0,"40");
        Score opponentPlayerScore = new Score(0,0,"30");

        MatchScore matchScore = new MatchScore(currentPlayerScore, opponentPlayerScore);

        matchScore.addPoints();


        assertAll(
                () -> assertEquals("0", opponentPlayerScore.getPoints()),
                () -> assertEquals("0", currentPlayerScore.getPoints())
        );

    }


}
