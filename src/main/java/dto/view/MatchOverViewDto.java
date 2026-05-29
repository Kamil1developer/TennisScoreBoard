package dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class MatchOverViewDto {

        // Инфикс 'View' можно удалить из названия класса — этот контекст понятен из названия пакета.

        // Более понятным было бы название 'OngoingMatchResultDto'.

        // Для DTO идеально подходит record.

        private final String winnerName;
        private final int winnerSets;
        private final String loserName;
        private final int loserSets;
}

