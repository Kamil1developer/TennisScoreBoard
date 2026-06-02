package entity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Fetch;

@Entity
@Table(name = "matches")
@Getter
@Check(constraints = "first_player_id <> second_player_id AND winner_id IN (first_player_id, second_player_id)")

public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "first_player_id", nullable = false, updatable = false)
    private Player firstPlayer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "second_player_id", nullable = false, updatable = false)
    private Player secondPlayer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "winner_id", nullable = false, updatable = false)
    private Player winner;

    protected Match(){}
    public Match(Player firstPlayer, Player secondPlayer, Player winner){
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.winner = winner;
    }


}
