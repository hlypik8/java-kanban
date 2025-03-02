import org.junit.jupiter.api.Test;
import manager.Managers;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    //Проверка того, что утилитарный класс всегда возвращает проинициализированные
    // и готовые к работе экземпляры менеджеров (не null)
    @Test
    public void managersDefaultShouldNotBeNull(){
        assertNotNull(Managers.getDefault());
    }

    @Test
    public void managersDefaultHistoryShouldNotBeNull(){
        assertNotNull(Managers.getDefaultHistory());
    }
}