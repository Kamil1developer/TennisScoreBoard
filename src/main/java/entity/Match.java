package entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Matches")
@Getter
@Setter
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Player1")
    private Player firstPlayerId;

    @ManyToOne
    @JoinColumn(name = "Player2")
    private Player secondPlayerId;

    @ManyToOne
    @JoinColumn(name = "Winner")
    private Player winnerId;

    protected Match(){}
    public Match(Player firstPlayerId, Player secondPlayerId, Player winnerId){
        this.firstPlayerId = firstPlayerId;
        this.secondPlayerId = secondPlayerId;
        this.winnerId = winnerId;
    }


}
