package scores;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Score{
    private int sets = 0;
    private int games = 0;
    private String points = "";
    private boolean isTiebreak = false;

    public Score(int sets, int games, String points){
        this.sets = sets;
        this.games = games;
        this.points = points;
    }

    public void addGames(int games) {
        this.games += games;
    }
    public void addSets(int sets) {
        this.sets += sets;
    }
}
