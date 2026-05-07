package matches;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import scores.Score;

@AllArgsConstructor
@Getter
@Setter
public class CurrentMatch{
    private final Long firstPlayerId;
    private final Long secondPlayerId;
    private final Score firstPlayerScore;
    private final Score secondPlayerScore;
}
