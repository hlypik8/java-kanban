package manager;

import java.io.IOException;

public class ManagerLoadException extends RuntimeException {
    ManagerLoadException(final String message) {
        super(message);
    }
}
