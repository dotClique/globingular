package globingular.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import globingular.core.GlobingularModule;
import globingular.core.World;
import globingular.persistence.PersistenceHandler;

public class GlobingularServiceTest {

    @Test
    public void testGetWorld() {
        GlobingularModule module = mock(GlobingularModule.class);
        PersistenceHandler persistence = mock(PersistenceHandler.class);
        String worldName = "hableWorld";
        World world = mock(World.class);

        when(persistence.getDefaultWorld(worldName)).thenReturn(world);

        GlobingularService service = new GlobingularService(module, persistence);

        assertEquals(world, service.getWorld(worldName));
    }
}
