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
    private Long firstPlayerId;

    @ManyToOne
    @JoinColumn(name = "Player2")
    private Long secondPlayerId;

    @ManyToOne
    @JoinColumn(name = "Winner")
    private Long winnerId;

    protected Match(){}
    public Match(Long firstPlayerId, Long secondPlayerId, Long winnerId){
        this.firstPlayerId = firstPlayerId;
        this.secondPlayerId = secondPlayerId;
        this.winnerId = winnerId;
    }


}
