package mujica.grammar;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2021/10/6", project = "va")
@CodeHistory(date = "2022/3/23", project = "infrastructure")
@CodeHistory(date = "2025/10/27")
public class LanguageException extends RuntimeException {

    private static final long serialVersionUID = 0xb72b82b8ea15a25fL;

    public LanguageException() {
        super();
    }

    public LanguageException(String message) {
        super(message);
    }

    public LanguageException(Throwable cause) {
        super(cause);
    }

    public LanguageException(String message, Throwable cause) {
        super(message, cause);
    }
}
