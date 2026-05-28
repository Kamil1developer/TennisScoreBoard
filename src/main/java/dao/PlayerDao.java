package dao;

import entity.Player;

public interface PlayerDao {

    // В Java по умолчанию все методы интерфейса являются public, поэтому можно не указывать этот модификатор явно.

    // Можно назвать стандартно — 'save'
    public Player insert(Player player);

    // Лучше возвращать Optional<Player>, чтобы этот метод никогда не возвращал null.
    public Player findByID(Long id);



    }