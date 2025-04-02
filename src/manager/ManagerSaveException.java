package manager;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {
    ManagerSaveException(final String message) {
        super(message);
    }
}
