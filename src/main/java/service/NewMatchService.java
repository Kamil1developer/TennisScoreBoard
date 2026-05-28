package service;

import dao.PlayerDao;
import dto.NewMatchRequestDto;
import entity.Player;
import matches.CurrentMatch;
import scores.Score;
import storages.CurrentMatchStorage;
import validator.NewMatchValidator;
import validator.TextValidator;

import java.util.UUID;

public class NewMatchService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // TODO: Класс отвечает за создание объекта текущего матча (доменной модели).
        // При этом он способствует смешению слоёв — сам использует зависимость от DAO и передаёт JPA Entity в доменную модель.
        // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
        // Этому классу должна быть не нужна зависимость PlayerDao.

    // Валидация имён игроков запускается из сервисного слоя, а не на "входе" в приложение.
        // Это не соответствует принципу быстрого отказа ("Fail Fast"):
        // Проверку корректности данных, пришедших от пользователя, следует проводить как можно раньше.
        // Валидация на уровне сервлета позволяет немедленно прервать обработку некорректного запроса и вернуть клиенту ошибку `400 Bad Request`.
        // Текущий подход заставляет приложение выполнять лишнюю работу, передавая заведомо невалидные данные дальше в сервисный слой.
        // Стоит запускать логику валидации из сервлета.

    private final PlayerDao playerDao;
    private final CurrentMatchStorage matchStorage;

    // Можно использовать @RequiredArgsConstructor
    public NewMatchService(PlayerDao playerDao, CurrentMatchStorage matchStorage) {
        this.playerDao = playerDao;
        this.matchStorage = matchStorage;
    }

    // Этот record не должен существовать по трём причинам:
        // - везде, где передаётся Players players можно просто передать Player firstPlayer, Player secondPlayer
        // - везде, где возвращается return new Players(firstPlayer, secondPlayer); можно просто вернуть результат метода,
            // который принимает двух игроков: return someMethod(firstPlayer, secondPlayer)
        // - этот класс создаёт текущие матчи, то есть доменные модели, в которых не должно быть JPA Entity
    private record Players(Player firstPlayer, Player secondPlayer){}

    public UUID createPlayers(NewMatchRequestDto requestDto){
        Players players = checkPlayerExists(requestDto);
        return  createMatch(players);

    }


    private Players checkPlayerExists(NewMatchRequestDto requestDto) {
        NewMatchValidator.validatePlayersAreDifferent(requestDto);

        String firstPlayerName = requestDto.firstPlayerName();
        String secondPlayerName = requestDto.secondPlayerName();

        TextValidator.validateLatinTextCharacters(firstPlayerName);
        TextValidator.validateLatinTextCharacters(secondPlayerName);

        Player firstPlayer = new Player(firstPlayerName);
        Player secondPlayer = new Player(secondPlayerName);

        firstPlayer = playerDao.insert(firstPlayer);

        // Если при сохранении второго игрока произойдёт ошибка, то операция окажется выполненной наполовину.
            // Такого не должно происходить. Это исправится автоматически, когда из доменных моделей исчезнут JPA сущности игроков.
        secondPlayer = playerDao.insert(secondPlayer);

        return new Players(firstPlayer, secondPlayer);

    }

    private UUID createMatch(Players players) {
        Player firstPlayer = players.firstPlayer;
        Player secondPlayer = players.secondPlayer;

        // Не стоит передавать ID из БД в доменные модели — для обозначения игроков лучше использовать доменные модели игроков или их имена.
        CurrentMatch currentMatch = new CurrentMatch(
                firstPlayer.getId(),
                secondPlayer.getId(),
                new Score(0, 0, "0"),
                new Score(0, 0, "0")
        );

        // Генерация ID для текущего матча должна происходить в хранилище матчей.
        UUID uuid = UUID.randomUUID();

        matchStorage.put(uuid, currentMatch);

        return  uuid;




    }


}
