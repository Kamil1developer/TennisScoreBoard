package dao;

import entity.Player;

public interface PlayerDao {
    public Player save(Player player);
    public Player findByID(Long id);



    }