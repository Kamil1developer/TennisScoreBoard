package model;

import lombok.RequiredArgsConstructor;
import service.OngoingMatchContext;

import java.util.UUID;

@RequiredArgsConstructor
public class MatchScore {
    private final Score currentPlayerScore;
    private final Score opponentPlayerScore;


    public void addPoints(){
        if (currentPlayerScore.isTiebreak() && opponentPlayerScore.isTiebreak()){
            addTiebreakPoint();
        }
        else {
            addRegularGamePoint();
        }
    }
    public void checkCountGames(){
        if (shouldStartTiebreak()){
            currentPlayerScore.setTiebreak(true);
            opponentPlayerScore.setTiebreak(true);
        }
        else if (isSetWon()){
            currentPlayerScore.setGames(0);
            opponentPlayerScore.setGames(0);
            currentPlayerScore.addSets(1);
        }

    }
    private void addTiebreakPoint(){
        String points = currentPlayerScore.getPoints();
        String opponentPoints = opponentPlayerScore.getPoints();

        int numberPoints = Integer.parseInt(points) + 1;
        currentPlayerScore.setPoints(String.valueOf(numberPoints));

        int numberOpponentPoints = Integer.parseInt(opponentPoints);

        if ((numberPoints - numberOpponentPoints >= 2) &&
                (numberPoints >= 7)){
            currentPlayerScore.setTiebreak(false);
            opponentPlayerScore.setTiebreak(false);
            currentPlayerScore.setGames(0);
            opponentPlayerScore.setGames(0);
            currentPlayerScore.setPoints("0");
            opponentPlayerScore.setPoints("0");
            currentPlayerScore.addSets(1);
        }


    }

    private  void addRegularGamePoint(){
        String points = currentPlayerScore.getPoints();
        String opponentPoints = opponentPlayerScore.getPoints();

        if (points.equals("0")) {
            currentPlayerScore.setPoints("15");
        }
        else if (points.equals("15")) {
            currentPlayerScore.setPoints("30");
        }
        else if (points.equals("30")) {
            currentPlayerScore.setPoints("40");
        }
        else if (points.equals("40") && (!opponentPoints.equals("40") && !opponentPoints.equals("AD"))) {
            currentPlayerScore.setPoints("0");
            opponentPlayerScore.setPoints("0");
            currentPlayerScore.addGames(1);
        }
        else if (points.equals("40") && opponentPoints.equals("AD")) {
            currentPlayerScore.setPoints("40");
            opponentPlayerScore.setPoints("40");
        }
        else if (points.equals("40") && opponentPoints.equals("40")) {
            currentPlayerScore.setPoints("AD");
        }
        else if (points.equals("AD") && opponentPoints.equals("40")) {
            currentPlayerScore.addGames(1);
            currentPlayerScore.setPoints("0");
            opponentPlayerScore.setPoints("0");
        }
        else if (points.equals("AD") && opponentPoints.equals("AD")) {
            currentPlayerScore.setPoints("40");
            opponentPlayerScore.setPoints("40");
        }
    }
    public boolean shouldStartTiebreak(){
        return (currentPlayerScore.getGames() == 6 && opponentPlayerScore.getGames() == 6);
    }
    private boolean isSetWon(){
        return (currentPlayerScore.getGames() == 6 && opponentPlayerScore.getGames() < 5 ||
                currentPlayerScore.getGames() == 7 && opponentPlayerScore.getGames() == 5);
    }
}
