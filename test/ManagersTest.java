import org.junit.jupiter.api.Test;
import manager.Managers;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void managersDefaultShouldNotBeNull(){
        assertNotNull(Managers.getDefault());
    }

    @Test
    public void managersDefaultHistoryShouldNotBeNull(){
        assertNotNull(Managers.getDefaultHistory());
    }
}