package scores;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Score{
    private int sets = 0;
    private int games = 0;
    private String points = "";
    private boolean ad = false;

    public void addGames(int games) {
        this.games += games;
    }
}
