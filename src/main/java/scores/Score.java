package scores;

import lombok.Getter;
import lombok.Setter;

@Getter


public class Score{

    // Более уместным было бы разместить класс в пакете 'model'.
        // (см. файл "model-types.md" в этом же пакете)

    // TODO: Класс является анемичной моделью — он является лишь контейнером для данных, а вся логика находится в сервисном слое.
        // Если бы у класса вместо простых сеттеров были методы, совершающие необходимую работу над полями,
        // это больше соответствовало бы ООП стилю и обязанности класса (в роли доменной модели).
        // Также, эту часть логики было бы легче тестировать.
        // (см. файл "reach-anemic-model.md" в этом же пакете)

    // TODO: Класс хранит счёт в гейме в переменной типа String, что позволяет установить любое некорректное значение (например, "-999" или "@#$%").
        // Поскольку в гейме особый счёт с ограниченным набором значений, ООП подходом было бы
        // создать специальный enum с константами ZERO, FIFTEEN, THIRTY, FORTY, ADVANTAGE для хранения счёта в гейме.

    // TODO: Класс отвечает за хранение очков на всех этапах игрового процесса в матче — это слишком большая ответственность
        // для одного класса и нарушает SRP (Single Responsibility Principle).
        // Лучшим решением в этом направлении было бы, чтобы за счёт на каждом уровне (матч-сет-гейм) отвечал отдельный класс.
        // Такой подход больше соответствовал бы ООП-стилю и принципу единственной ответственности для каждого класса.

    // Хотя `0` для начального счёта является интуитивно понятным значением, явное использование литералов ("магических чисел") в коде не считается хорошей практикой.
        // Если это значение потребуется изменить, придётся искать все его вхождения. Вынесение в именованную константу улучшает читаемость и упрощает поддержку.

    // Все "магические числа" лучше вынести в именованные константы

    private int sets = 0;
    private int games = 0;
    private String points = "";

    @Setter // TODO: сеттер позволяет бесконтрольно изменять состояние модели
    private boolean isTiebreak = false;

    public Score(int sets, int games, String points){
        if (isValidInitialState(sets, games, points)) {
            this.sets = sets;
            this.games = games;
            this.points = points;
        }
    }

    private boolean isValidInitialState(int sets, int games, String points){
        return isValidGames(games) && isValidSets(sets) && isValidPoints(points);
    }

    private boolean isValidGames(int games){
        return games >= 0;
    }

    private boolean isValidSets(int sets){
        return sets >= 0;
    }

    private boolean isValidPoints(String points){

        // (просто для справки) Здесь больше подошло бы регулярное выражение, перечисляющее допустимые варианты: "^(0|15|30|40|AD)$".
            // Но для хранения счёта в гейме идеально подходит enum с необходимыми значениями.
        return !points.matches(".*[A-Za-z].*") || points.equals("AD");
    }

    // TODO: сеттер позволяет бесконтрольно изменять состояние модели
    public void setPoints(String points){
        if (isValidPoints(points)){
            this.points = points;
        }
    }

    // TODO: сеттер позволяет бесконтрольно изменять состояние модели
    public void setGames(int games){
        if (games >= 0){
            this.games = games;
        }
    }

    // Метод позволяет увеличивать счёт до пределов переменной int
    public void addGames(int games) {
        this.games += games;
    }

    // Метод позволяет увеличивать счёт до пределов переменной int
    public void addSets(int sets) {
        this.sets += sets;
    }
}
