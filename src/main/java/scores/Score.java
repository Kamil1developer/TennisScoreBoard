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

    public void addGames(int games) {
        this.games += games;
    }
    public void addSets(int sets) {
        this.sets += sets;
    }
}
