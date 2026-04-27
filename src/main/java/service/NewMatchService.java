package service;

import dao.impl.PlayerDao;

public class NewMatchService {
    private final PlayerDao playerDao;

    public NewMatchService(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }
}
