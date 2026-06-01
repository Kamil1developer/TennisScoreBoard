package dao;

import entity.Player;

public interface PlayerDao {
    Player save(Player player);
    Player findByID(Long id);



    }