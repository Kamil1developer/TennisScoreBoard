package dao;
import entity.Match;

public interface MatchDao {
    public void insert(Match match);
    public void findById();
    public void findAll();
}
