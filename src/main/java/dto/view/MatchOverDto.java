package dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatchOverDto {
        private final String winnerName;
        private final int winnerSets;
        private final String loserName;
        private final int loserSets;
}

