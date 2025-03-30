//Утилитарный класс Manager для упровления всеми менеждерами. На нём будет лежать вся ответственность
// за создание менеджера задач.
package manager;

import java.io.File;

public class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    //Добавляем в утилитарный класс менеджер файлов
    public static FileBackedTaskManager getDefaultFile(File file) {
        return new FileBackedTaskManager(file);
    }
}
