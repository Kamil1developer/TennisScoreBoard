package dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MatchRowViewDto{

    // Инфикс 'View' можно удалить из названия класса — этот контекст понятен из названия пакета.

    // Более понятным было бы название 'FinishedMatchDto'.

    // Для DTO идеально подходит record.

    private String firstPlayerName;
    private String secondPlayerName;
    private String winnerName;
}
