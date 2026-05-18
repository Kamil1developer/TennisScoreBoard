package dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MatchRowViewDto{
    private String firstPlayerName;
    private String secondPlayerName;
    private String winnerName;
}
