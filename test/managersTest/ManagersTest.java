package managersTest;

import org.junit.jupiter.api.Test;
import manager.Managers;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    //Проверка того, что утилитарный класс всегда возвращает проинициализированные
    // и готовые к работе экземпляры менеджеров (не null)
    @Test
    public void managersDefaultShouldNotBeNull() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    public void managersDefaultHistoryShouldNotBeNull() {
        assertNotNull(Managers.getDefaultHistory());
    }

    @Test
    public void managersGetDefaultFileManagerShouldNotBeNull() throws IOException {
        assertNotNull(Managers.getDefaultFileManager(File.createTempFile("Test", ".csv")));
    }
}