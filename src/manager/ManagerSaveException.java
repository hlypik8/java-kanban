package manager;

import java.io.IOException;

public class ManagerSaveException extends IOException {
    ManagerSaveException(final String message) {
        super(message);
    }
}
