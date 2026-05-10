package dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MatchOverViewDto {
    private String winnerName;
    private int winnerSets;
    private String loserName;
    private int loserSets;
}
