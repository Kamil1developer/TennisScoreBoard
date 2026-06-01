package matches;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import model.Score;

@AllArgsConstructor
@Getter
@Setter
public class CurrentMatch{
    private final Long firstPlayerId;
    private final Long secondPlayerId;
    private final Score firstPlayerScore;
    private final Score secondPlayerScore;
}
