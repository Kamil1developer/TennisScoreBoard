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

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // TODO: Класс нарушает Принцип единой ответственности. Он занимается:
    // - сохранением матчей
    // - сам определяет победителя в матче
    // - готовит DTO для передачи во View

    private final MatchDao matchDao;
    private final PlayerDao playerDao;
    private final CurrentMatchStorage currentMatchStorage;

    // Можно использовать @RequiredArgsConstructor
    public CompletedMatchService(MatchDao matchDao, PlayerDao playerDao, CurrentMatchStorage currentMatchStorage) {
        this.matchDao = matchDao;
        this.playerDao = playerDao;
        this.currentMatchStorage = currentMatchStorage;
    }

    // Клиентский код этого метода (тот, который его вызывает) не должен ничего знать о JPA Entity (Player).
        // Этот метод должен принимать доменную модель матча, извлекать из неё необходимые данные, и сохранять матч.
    // Опечатка в названии safe —> save
    public void safe(Player firstPlayer, Player secondPlayer, Player winner){
        Match match = new Match(firstPlayer, secondPlayer, winner);
        matchDao.insert(match);
    }

    // Больше подошло бы название createOngoingMatchContext
    private OngoingMatchContext loadOngoingMatchContext(UUID matchId){
        CurrentMatch currentMatch = currentMatchStorage.getMap().get(matchId);

        Long firstPlayerId = currentMatch.getFirstPlayerId();
        Long secondPlayerId = currentMatch.getSecondPlayerId();

        Player firstPlayer = playerDao.findByID(firstPlayerId);
        Player secondPlayer = playerDao.findByID(secondPlayerId);

        return new OngoingMatchContext(currentMatch, matchId, firstPlayer, secondPlayer);
    }

    // Не существует (не должно существовать) нормальной ситуации, в которой объект завершённого матча
        // не мог бы быть преобразован в DTO. Поэтому этот метод не должен возвращать Optional.
    public Optional<MatchOverViewDto> safeCompletedMatch(int firstPlayerSets, int secondPlayerSets, UUID matchId) {
        OngoingMatchContext matchContext = loadOngoingMatchContext(matchId);
        CurrentMatch currentMatch = currentMatchStorage.getMap().get(matchId);

        // Логика в этом if-else во многом дублируется для каждого игрока.
            // Это нарушает принцип DRY (Don't Repeat Yourself) и усложняет поддержку.
            // Если в логику потребуется внести изменение, его придётся делать в двух местах, что увеличивает риск ошибок.
            // В таких случаях стоит придумать, как избавиться от дублирования.
        // Логика определения победителя должна находиться в доменной модели.
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
