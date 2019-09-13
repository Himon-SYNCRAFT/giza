package pl.syncraft.giza.exceptions;

/**
 * @author Daniel Zawlocki
 * @date 2019/09/11
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String errorMessage) {
        super(errorMessage);
    }

    public ValidationException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
