package dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaginatedMatchesViewDto {

    // Инфикс 'View' можно удалить из названия класса — этот контекст понятен из названия пакета.

    // Для DTO идеально подходит record.

    // Возможно, более подходящим было бы название 'MatchesPageDto'.

    private final List<MatchRowViewDto> matchesList; // Суффикс 'List' не нужен в названии
    private final int currentPage;
    private final boolean hasPrevious;
    private final boolean hasNext;

}
