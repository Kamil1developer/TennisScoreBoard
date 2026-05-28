package entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Matches") // "Matches" является зарезервированным словом в некоторых СУБД. Здесь проблем не будет, но лучше не выбирать такие названия. (см. файл "sql-keywords.md" в этом же пакете)
@Getter
@Setter // TODO: сеттеры не нужны — позволяют создать объект с установленным id или изменить состав игроков после создания (например сделать постороннего игрока победителем)
public class Match {

    // Стоит добавить проверки, что игроки разные и победитель один из игроков. Например, через аннотацию org.hibernate.annotations.Check над классом.

    // Связи `@ManyToOne` не имеют явного указания о стратегии загрузки.
        // По умолчанию для `@ManyToOne` используется `FetchType.EAGER`, что приводит к немедленной загрузке связанных сущностей при загрузке `Match`.
        // Это может вызывать проблемы производительности (N+1 запросов) и излишнюю загрузку данных, особенно если связанные объекты не всегда нужны.

    // Для обязательных полей стоит добавить `optional = false` в `@ManyToOne` или `nullable = false` в `@JoinColumn` (можно добавить оба параметра).
        // Целостность данных должна обеспечиваться на всех уровнях: в приложении (валидация) и в БД (constraints). Отсутствие ограничений в БД означает,
        // что данные могут быть испорчены из-за ошибок в приложении или при прямом доступе к БД.
        //
        // А также можно добавить атрибут `updatable = false`. Это атрибут запрещает изменять колонку после её первоначальной вставки.
        // Игроки матча и победитель не должны меняться, поэтому эти колонки можно защитить от обновлений.

    // Колонки игроков и победителя в `@JoinColumn` названы `Player1`, `Player2`, `Winner`.
        // Для колонок, хранящих внешний ключ, уместно добавлять суффикс `_id`, чтобы было очевидно, что в них хранится идентификатор, а не какая-то другая информация.
        // А также использовать более идиоматичный в SQL стиль lower_snake_case для названий колонок в БД.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Стоит добавить FetchType.LAZY, а также optional = false
    @JoinColumn(name = "Player1") // 1. Можно добавить nullable = false и updatable = false. 2. Числительные в названиях колонок тоже можно писать буквами для единообразия.
    private Player firstPlayerId; // Поле хранит объект типа Player, поэтому суффикс 'Id' не нужен в названии.

    @ManyToOne // Стоит добавить FetchType.LAZY, а также optional = false
    @JoinColumn(name = "Player2") // 1. Можно добавить nullable = false и updatable = false. 2. Числительные в названиях колонок тоже можно писать буквами для единообразия.
    private Player secondPlayerId; // Поле хранит объект типа Player, поэтому суффикс 'Id' не нужен в названии.

    @ManyToOne // Стоит добавить FetchType.LAZY, а также optional = false
    @JoinColumn(name = "Winner") // Можно добавить nullable = false и updatable = false
    private Player winnerId; // Поле хранит объект типа Player, поэтому суффикс 'Id' не нужен в названии.

    // Можно использовать аннотацию @NoArgsConstructor(access = AccessLevel.PROTECTED) (как сейчас в Player).
    protected Match(){}

    // Здесь, как и в полях, не нужен суффикс 'Id' в названиях объектов типа Player.
    public Match(Player firstPlayerId, Player secondPlayerId, Player winnerId){
        this.firstPlayerId = firstPlayerId;
        this.secondPlayerId = secondPlayerId;
        this.winnerId = winnerId;
    }


}
