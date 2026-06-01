package dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MatchRowDto {
    private String firstPlayerName;
    private String secondPlayerName;
    private String winnerName;
}
