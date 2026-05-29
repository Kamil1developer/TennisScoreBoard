package dto.view;

import scores.Score;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CurrentMatchViewDto {

    // Инфикс 'View' можно удалить из названия класса — этот контекст понятен из названия пакета.

    // Для DTO идеально подходит record.

    // Класс содержит доменные модели(`Score`). Это смешивает слои. Все поля в DTO тоже должны быть DTO или примитивными/простыми типами.
        // (см. файл "model-types.md" в этом же пакете)

    private final UUID uuid;
    private final PlayerViewDto firstPlayerDto;
    private final PlayerViewDto secondPlayerDto;
    private final Score firstPlayerScore;
    private final Score secondPlayerScore;
}
