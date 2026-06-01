package dto.view;

import model.Score;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CurrentMatchDto {
    private final UUID uuid;
    private final PlayerDto firstPlayerDto;
    private final PlayerDto secondPlayerDto;
    private final Score firstPlayerScore;
    private final Score secondPlayerScore;
}
