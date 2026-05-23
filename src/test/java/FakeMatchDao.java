import dao.MatchDao;
import entity.Match;

import java.util.List;
import java.util.Optional;

public class FakeMatchDao implements MatchDao {
    @Override
    public void insert(Match match) {

    }

    @Override
    public void findById() {

    }

    @Override
    public Optional<List<Match>> findAll() {
        return Optional.empty();
    }
}
