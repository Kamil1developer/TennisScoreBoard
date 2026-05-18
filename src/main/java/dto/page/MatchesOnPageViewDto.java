package dto.page;

import dto.view.PaginatedMatchesViewDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class MatchesOnPageViewDto {
    private final List<PaginatedMatchesViewDto> paginatedMatchesList;
}
