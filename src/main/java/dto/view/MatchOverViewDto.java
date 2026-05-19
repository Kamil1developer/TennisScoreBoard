package dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class MatchOverViewDto {
        private final String winnerName;
        private final int winnerSets;
        private final String loserName;
        private final int loserSets;
}

