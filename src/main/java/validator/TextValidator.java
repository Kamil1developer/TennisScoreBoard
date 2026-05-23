package validator;

import exceptions.NewMatchValidationException;
import exceptions.TextValidationException;

public class TextValidator {
    private static final String LATIN_NAME_REGEX = "^[a-zA-Z ]+$";
    private static final String INVALID_PLAYER_NAME_CHARACTERS_ERROR = "Имя игрока должно быть написано латиницей";

    private TextValidator() {}

    public static void validateLatinTextCharacters(String name){
        if (!name.matches(LATIN_NAME_REGEX)){
            throw new TextValidationException(INVALID_PLAYER_NAME_CHARACTERS_ERROR);
        }
    }
}
