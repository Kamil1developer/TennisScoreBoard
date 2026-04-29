package service;

import dao.PlayerDao;
import dto.NewMatchRequestDto;
import entity.Player;
import matches.CurrentMatch;
import scores.Scores;
import validator.NewMatchValidator;

public class NewMatchService {
    private final PlayerDao playerDao;

    public NewMatchService(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    public void checkPlayerExists(NewMatchRequestDto requestDto) {
        NewMatchValidator.validatePlayersAreDifferent(requestDto);

        String firstPlayerName = requestDto.firstPlayerName();
        String secondPlayerName = requestDto.secondPlayerName();

        Player firstPlayer = new Player(firstPlayerName);
        Player secondPlayer = new Player(secondPlayerName);

        firstPlayer = playerDao.insert(firstPlayer);
        secondPlayer = playerDao.insert(secondPlayer);

        createMatch(firstPlayer, secondPlayer);


    }

    public void createMatch(Player firstPlayer, Player secondPlayer) {
        CurrentMatch currentMatch = new CurrentMatch(
                firstPlayer.getId(),
                secondPlayer.getId(),
                new Scores(0, 0, 0),
                new Scores(0, 0, 0)
        );
    }


}
