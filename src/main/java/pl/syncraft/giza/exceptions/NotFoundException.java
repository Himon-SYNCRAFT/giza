package pl.syncraft.giza.exceptions;

/**
 * @author Daniel Zawlocki
 * @date 2019/09/05
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public NotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
