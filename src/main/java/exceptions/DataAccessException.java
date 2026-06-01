package exceptions;

public class DataAccessException extends RuntimeException {

    public DataAccessException() {
        super("Ошибка бд");
    }
}
