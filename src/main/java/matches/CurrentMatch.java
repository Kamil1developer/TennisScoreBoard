package matches;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import scores.Scores;

@AllArgsConstructor
@Getter
@Setter
public class CurrentMatch{
    private final Long firstPlayerId;
    private final Long secondPlayerId;
    private final Scores firstPlayerScores;
    private final Scores secondPlayerScores;
}
