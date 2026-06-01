package dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaginatedMatchesDto {
    private final List<MatchRowDto> matchesList;
    private final int currentPage;
    private final boolean hasPrevious;
    private final boolean hasNext;

}
