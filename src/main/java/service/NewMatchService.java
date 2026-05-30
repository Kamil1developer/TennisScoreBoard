package service;

import dao.PlayerDao;
import dto.NewMatchRequestDto;
import entity.Player;
import matches.CurrentMatch;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import scores.Score;
import storages.CurrentMatchStorage;
import transaction.TransactionManager;
import validator.NewMatchValidator;
import validator.TextValidator;

import java.util.UUID;

public class NewMatchService {
    private final PlayerDao playerDao;
    private final CurrentMatchStorage matchStorage;
    private final TransactionManager transactionManager;
    public NewMatchService(PlayerDao playerDao, CurrentMatchStorage matchStorage, TransactionManager transactionManager) {
        this.playerDao = playerDao;
        this.matchStorage = matchStorage;
        this.transactionManager = transactionManager;
    }

    private record Players(Player firstPlayer, Player secondPlayer){}

    public UUID createPlayers(NewMatchRequestDto requestDto){
        Players players = checkPlayerExists(requestDto);
        return  createMatch(players);

    }


    private Players checkPlayerExists(NewMatchRequestDto requestDto) {
        NewMatchValidator.validatePlayersAreDifferent(requestDto);

        String firstPlayerName = requestDto.firstPlayerName();
        String secondPlayerName = requestDto.secondPlayerName();

        TextValidator.validateLatinTextCharacters(firstPlayerName);
        TextValidator.validateLatinTextCharacters(secondPlayerName);

        Player firstPlayer = new Player(firstPlayerName);
        Player secondPlayer = new Player(secondPlayerName);

        transactionManager.executeInTransaction(() -> {
            playerDao.save(firstPlayer);
            playerDao.save(secondPlayer);
            return null;
        });

        return new Players(firstPlayer, secondPlayer);

    }

    private UUID createMatch(Players players) {
        Player firstPlayer = players.firstPlayer;
        Player secondPlayer = players.secondPlayer;

        CurrentMatch currentMatch = new CurrentMatch(
                firstPlayer.getId(),
                secondPlayer.getId(),
                new Score(0, 0, "0"),
                new Score(0, 0, "0")
        );

        UUID uuid = UUID.randomUUID();

        matchStorage.put(uuid, currentMatch);

        return  uuid;




    }


}
