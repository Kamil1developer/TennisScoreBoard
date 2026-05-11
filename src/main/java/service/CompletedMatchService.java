package service;

import dao.MatchDao;
import entity.Match;

public class CompletedMatchService {

    private final MatchDao matchDao;
    public CompletedMatchService(MatchDao matchDao){
        this.matchDao = matchDao;
    }

    public void safe(Long firstPlayerId, Long secondPlayerId, Long winnerId){
        Match match = new Match(firstPlayerId, secondPlayerId, winnerId);
        matchDao.insert(match);


    }

}
