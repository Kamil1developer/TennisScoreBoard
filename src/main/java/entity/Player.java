package entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Players") // можно задать индекс через аннотацию, чтобы у него было понятное имя — @Table(name = "Players", indexes = @Index(...))
@Getter
@Setter // TODO: сеттеры не нужны — позволяют создать объект с установленным id или изменить имя игрока после создания
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player {

    // TODO: Нет индекса на поле name. Стоит добавить через @Column(name = "Name", unique = true)
        // или через аннотацию над классом @Table(name = "Players", indexes = @Index(...))

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name", nullable = false) // Можно добавить length = 50, чтобы задать ограничения на уровне БД
    private String name;

    public Player(String name){
        this.name = name;
    }

}
