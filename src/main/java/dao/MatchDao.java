package dao;
import entity.Match;

import java.util.List;
import java.util.Optional;

public interface MatchDao {
    public void insert(Match match);
    public void findById();
    public Optional<List<Match>> findAll();
}
