package dao;

import entity.Player;

import java.util.List;

public interface PlayerDao {

    public List<Player> findAll();
    public Player insert(Player player);



}