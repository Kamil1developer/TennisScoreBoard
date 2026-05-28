package matches;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import scores.Score;

@AllArgsConstructor
@Getter
@Setter // TODO: сеттеры позволяют бесконтрольно изменять состояние модели
public class CurrentMatch{

    // Более уместным было бы разместить класс в пакете 'model'.
        // (см. файл "model-types.md" в этом же пакете)

    // TODO: Класс является анемичной моделью — он является лишь контейнером для данных, а вся логика находится в сервисном слое.
        // Если бы у класса вместо простых сеттеров были методы, совершающие необходимую работу над полями,
        // это больше соответствовало бы ООП стилю и обязанности класса (в роли доменной модели).
        // Также, эту часть логики было бы легче тестировать.
        // (см. файл "reach-anemic-model.md" в этом же пакете)

    // ID игроков в этом объекте — это ID из БД. Внутренняя информация из БД не должна "протекать" в доменные модели.
        // Вместо этого можно использовать ID 1 и 2 для первого и второго игрока соответственно, или доменные модели игроков, или просто их имена.

    private final Long firstPlayerId;
    private final Long secondPlayerId;
    private final Score firstPlayerScore;
    private final Score secondPlayerScore;
}
