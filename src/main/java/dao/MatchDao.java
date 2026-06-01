package dao;
import entity.Match;

import java.util.List;
import java.util.Optional;

public interface MatchDao {
    void save(Match match);
    List<Match> findAll();
}
