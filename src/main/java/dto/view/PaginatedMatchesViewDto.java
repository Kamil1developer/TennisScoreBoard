package dto.view;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class PaginatedMatchesViewDto {
    private final List<MatchRowViewDto> matchesList;
    private final int currentPage;
    private final boolean hasPrevious;
    private final boolean hasNext;

}
