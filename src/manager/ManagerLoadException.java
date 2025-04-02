package manager;

import java.io.IOException;

public class ManagerLoadException extends IOException {
    ManagerLoadException(final String message) {
        super(message);
    }
}
