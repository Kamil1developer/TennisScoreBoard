package dto.view;

import lombok.Setter;

public record MatchOverViewDto (
        String winnerName,
        int winnerSets,
        String loserName,
        int loserSets){}
