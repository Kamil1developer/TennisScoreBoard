package entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "players", indexes = @Index(name = "idx_players_name", columnList = "name"))
@Getter

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    public Player(String name){
        this.name = name;
    }

}
