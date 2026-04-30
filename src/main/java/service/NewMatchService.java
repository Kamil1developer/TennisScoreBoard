package service;

import dao.PlayerDao;
import dto.NewMatchRequestDto;
import entity.Player;
import matches.CurrentMatch;
import scores.Scores;
import validator.NewMatchValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NewMatchService {
    private final PlayerDao playerDao;
    public NewMatchService(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    private record Players(Player firstPlayer, Player secondPlayer){}

    public void createPlayer(NewMatchRequestDto requestDto){
        Players players = checkPlayerExists(requestDto);
//        createMatch()

    }


    public Players checkPlayerExists(NewMatchRequestDto requestDto) {
        NewMatchValidator.validatePlayersAreDifferent(requestDto);

        String firstPlayerName = requestDto.firstPlayerName();
        String secondPlayerName = requestDto.secondPlayerName();

        Player firstPlayer = new Player(firstPlayerName);
        Player secondPlayer = new Player(secondPlayerName);

        firstPlayer = playerDao.insert(firstPlayer);
        secondPlayer = playerDao.insert(secondPlayer);

        return new Players(firstPlayer, secondPlayer);

    }

    public UUID createMatch(Players players) {
        Player firstPlayer = players.firstPlayer;
        Player secondPlayer = players.secondPlayer;

        CurrentMatch currentMatch = new CurrentMatch(
                firstPlayer.getId(),
                secondPlayer.getId(),
                new Scores(0, 0, 0),
                new Scores(0, 0, 0)
        );

        UUID uuid = UUID.randomUUID();
        Map<UUID, CurrentMatch> currentMatchesMap = new HashMap<>();

        currentMatchesMap.put(uuid, currentMatch);

        return  uuid;




    }


}
