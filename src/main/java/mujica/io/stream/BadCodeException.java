package mujica.io.stream;

import mujica.reflect.modifier.CodeHistory;

import java.io.IOException;

@CodeHistory(date = "2025/5/18")
public class BadCodeException extends IOException {

    public BadCodeException() {
        super();
    }

    public BadCodeException(String message) {
        super(message);
    }
}
