package dao;

import entity.Player;

import java.util.List;

public interface PlayerDao {
    public Player insert(Player player);
    public Player findByID(Long id);



    }