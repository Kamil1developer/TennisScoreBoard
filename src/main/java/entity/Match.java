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
    private Player firstPlayerName;

    @ManyToOne
    @JoinColumn(name = "Player2")
    private Player secondPlayerName;

    @ManyToOne
    @JoinColumn(name = "Winner")
    private Player winner;
}
