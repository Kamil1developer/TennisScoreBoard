import dao.PlayerDao;
import entity.Player;

public class FakePlayerDao implements PlayerDao {
    @Override
    public Player save(Player player) {
        return null;
    }

    @Override
    public Player findByID(Long id) {
        return null;
    }
}
