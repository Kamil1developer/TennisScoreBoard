import dao.MatchDao;
import entity.Match;

import java.util.List;
import java.util.Optional;

public class FakeMatchDao implements MatchDao {
    @Override
    public void save(Match match) {

    }


    @Override
    public List<Match> findAll() {
        return List.of();
    }
}
