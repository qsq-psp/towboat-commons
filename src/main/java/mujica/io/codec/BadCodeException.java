package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;

import java.io.IOException;

@CodeHistory(date = "2025/5/18")
public class BadCodeException extends IOException {

    private static final long serialVersionUID = 0x2E3EFC0654034D36L;

    public BadCodeException() {
        super();
    }

    public BadCodeException(String message) {
        super(message);
    }
}
