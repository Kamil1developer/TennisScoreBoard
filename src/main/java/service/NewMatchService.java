package service;

import dao.impl.PlayerDao;
import dto.NewMatchRequestDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import validator.NewMatchValidator;

import java.io.IOException;

public class NewMatchService {
    private final PlayerDao playerDao;

    public NewMatchService(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }
    public void checkPlayerExists(NewMatchRequestDto requestDto){
        NewMatchValidator.validatePlayersAreDifferent(requestDto);
    }


}
