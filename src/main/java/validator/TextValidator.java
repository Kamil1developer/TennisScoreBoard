package validator;

import exceptions.NewMatchValidationException;
import exceptions.TextValidationException;

public class TextValidator {

    // Больше подошло бы название NameValidator.

    // Если класс задуман как утилитный, то стоит делать его final.

    // Реальные имена могут содержать дефисы, точки и апострофы. А также было бы уместно разрешить кириллицу.
    // Регулярное выражение можно хранить в объекте java.util.regex.Pattern
    private static final String LATIN_NAME_REGEX = "^[a-zA-Z ]+$";
    private static final String INVALID_PLAYER_NAME_CHARACTERS_ERROR = "Имя игрока должно быть написано латиницей";

    private TextValidator() {}

    // Нет проверки на null. Если на вход придёт null, то метод выбросит NullPointerException
    public static void validateLatinTextCharacters(String name){
        if (!name.matches(LATIN_NAME_REGEX)){
            throw new TextValidationException(INVALID_PLAYER_NAME_CHARACTERS_ERROR);
        }
    }
}
