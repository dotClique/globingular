package globingular.restserver;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import globingular.core.World;
import globingular.persistence.PersistenceHandler;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class GlobingularServiceTest {

    private static HttpServer server;
    private static WebTarget target;
    ObjectMapper objectMapper = new GlobingularObjectMapperProvider().getContext(getClass());

    @BeforeAll
    public static void setUp() {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        target = c.target(Main.BASE_URI);
    }

    @AfterAll
    public static void tearDown() {
        server.shutdownNow();
    }

    @Test
    public void testGetWorld() throws JsonMappingException, JsonProcessingException {
        Response response = target.path("globingular").path("Earth").request().get();

        assertEquals(200, response.getStatus());

        World w = objectMapper.readValue(response.readEntity(String.class),
                World.class);

        assertEquals("Earth", w.getWorldName());
    }
}
