package dao;

import entity.Player;

public interface PlayerDao {
    public Player insert(Player player);
    public Player findByID(Long id);



    }