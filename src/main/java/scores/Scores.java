package scores;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Scores{
    private int sets = 0;
    private int games = 0;
    private int points = 0;
    private boolean ad = false;

    public void addGames(int games) {
        this.games += games;
    }
}
