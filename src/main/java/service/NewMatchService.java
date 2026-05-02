package service;

import dao.PlayerDao;
import dto.NewMatchRequestDto;
import entity.Player;
import matches.CurrentMatch;
import scores.Scores;
import storages.CurrentMatchStorage;
import validator.NewMatchValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NewMatchService {
    private final PlayerDao playerDao;
    private final CurrentMatchStorage matchStorage;
    public NewMatchService(PlayerDao playerDao, CurrentMatchStorage matchStorage) {
        this.playerDao = playerDao;
        this.matchStorage = matchStorage;
    }

    private record Players(Player firstPlayer, Player secondPlayer){}

    public UUID createPlayers(NewMatchRequestDto requestDto){
        Players players = checkPlayerExists(requestDto);
        return  createMatch(players);

    }


    private Players checkPlayerExists(NewMatchRequestDto requestDto) {
        NewMatchValidator.validatePlayersAreDifferent(requestDto);

        String firstPlayerName = requestDto.firstPlayerName();
        String secondPlayerName = requestDto.secondPlayerName();

        Player firstPlayer = new Player(firstPlayerName);
        Player secondPlayer = new Player(secondPlayerName);

        firstPlayer = playerDao.insert(firstPlayer);
        secondPlayer = playerDao.insert(secondPlayer);

        return new Players(firstPlayer, secondPlayer);

    }

    private UUID createMatch(Players players) {
        Player firstPlayer = players.firstPlayer;
        Player secondPlayer = players.secondPlayer;

        CurrentMatch currentMatch = new CurrentMatch(
                firstPlayer.getId(),
                secondPlayer.getId(),
                new Scores(0, 0, 0,false),
                new Scores(0, 0, 0,false)
        );

        UUID uuid = UUID.randomUUID();

        matchStorage.put(uuid, currentMatch);

        return  uuid;




    }


}
