package model;

import lombok.Getter;
import lombok.Setter;

@Getter


public class Score{
    private int sets = 0;
    private int games = 0;
    private String points = "";
    @Setter
    private boolean isTiebreak = false;

    public Score(int sets, int games, String points){
        if (isValidInitialState(sets, games, points)) {
            this.sets = sets;
            this.games = games;
            this.points = points;
        }
    }

    private boolean isValidInitialState(int sets, int games, String points){
        return isValidGames(games) && isValidSets(sets) && isValidPoints(points);
    }

    private boolean isValidGames(int games){
        return games >= 0;
    }

    private boolean isValidSets(int sets){
        return sets >= 0;
    }

    private boolean isValidPoints(String points){
        return !points.matches(".*[A-Za-z].*") || points.equals("AD");
    }

    public void setPoints(String points){
        if (isValidPoints(points)){
            this.points = points;
        }
    }
    public void setGames(int games){
        if (games >= 0){
            this.games = games;
        }
    }

    public void addGames(int games) {
        this.games += games;
    }
    public void addSets(int sets) {
        this.sets += sets;
    }



}
