import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scores.Score;
import service.CompletedMatchService;
import service.OngoingMatchService;
import storages.CurrentMatchStorage;

import static org.junit.jupiter.api.Assertions.*;

public class OngoingMatchServiceTest {

    // После проведения декомпозиции и рефакторинга доменных моделей, также следует изменить тесты для этой части логики.

    // Невозможность протестировать OngoingMatchService без классов DAO превращает юнит-тест в интеграционный.
        // Это исправится после переноса основной бизнес-логики в классы моделей.

    // Текущий набор тестов покрывает только часть возможных сценариев. Отсутствуют тесты на:
        // - Полный цикл игры при счете "больше-меньше" (AD -> Deuce -> AD -> Game).
        // - Выигрыш сета (например, при счете 5-4 по геймам).
        // - Выигрыш всего матча выигрышем второго сета.
        // - Корректную работу тай-брейка (выигрыш при счете 7-5, продолжение игры при 6-6 и т.д.).

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

        // Лучше явно проверять значение: assertEquals(1, currentPlayerGamesAfterPoint)
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

        // Также стоило бы проверить, что счет в очках стал "AD" - "40"
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
