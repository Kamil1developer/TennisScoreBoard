package dto.view;
import entity.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class PlayerViewDto {
    private final Long id;
    private final Player player;
}
