package dto.view;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchOverViewDto {
    private String winnerName;
    private int winnerSets;
    private String loserName;
    private int loserSets;
}
