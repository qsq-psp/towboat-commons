package mujica.io.view;

/**
 * Created on 2025/5/12.
 */
public class DataSizeMismatchException extends RuntimeException {

    private static final long serialVersionUID = 0xd3a1aba6440cb69dL;

    public DataSizeMismatchException() {
        super();
    }

    public DataSizeMismatchException(String message) {
        super(message);
    }
}
