package service;

import dao.PlayerDao;
import dto.NewMatchRequestDto;
import entity.Player;
import validator.NewMatchValidator;

public class NewMatchService {
    private final PlayerDao playerDao;

    public NewMatchService(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }
    public void checkPlayerExists(NewMatchRequestDto requestDto){
        NewMatchValidator.validatePlayersAreDifferent(requestDto);

        String firstPlayerName =  requestDto.firstPlayerName();
        String secondPlayerName = requestDto.secondPlayerName();

        Player firstPlayer = new Player(firstPlayerName);
        Player secondPlayer = new Player(secondPlayerName);

        playerDao.insert(firstPlayer);
        playerDao.insert(secondPlayer);

    }


}
