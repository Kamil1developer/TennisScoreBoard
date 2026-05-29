package validator;

import dto.NewMatchRequestDto;
import exceptions.NewMatchValidationException;

public class NewMatchValidator {

    // Если класс задуман как утилитный, то стоит делать его final.

    private static final String SAME_PLAYERS_ERROR = "Игрок не может играть сам с собой";


    private NewMatchValidator(){}

    public static void validatePlayersAreDifferent(NewMatchRequestDto requestDto){
        String firstPlayerName =  requestDto.firstPlayerName();
        String secondPlayerName = requestDto.secondPlayerName();

        // Можно проверять без учёта регистра: firstPlayerName.equalsIgnoreCase(secondPlayerName)
        if (firstPlayerName.equals(secondPlayerName)){
            throw new NewMatchValidationException(SAME_PLAYERS_ERROR);
        }
    }

}
