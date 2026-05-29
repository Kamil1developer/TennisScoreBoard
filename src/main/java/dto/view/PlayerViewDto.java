package dto.view;
import entity.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter // Сеттеры не используются и не нужны для DTO

public class PlayerViewDto {

    // Инфикс 'View' можно удалить из названия класса — этот контекст понятен из названия пакета.

    // Для DTO идеально подходит record.

    private final Long id;
    private final String name;
}
