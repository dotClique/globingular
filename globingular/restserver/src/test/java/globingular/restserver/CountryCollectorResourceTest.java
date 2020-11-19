package globingular.restserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import globingular.core.Country;
import globingular.core.CountryCollector;
import globingular.core.World;
import globingular.persistence.PersistenceHandler;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

public class CountryCollectorResourceTest {

    private static HttpServer server;
    private static WebTarget target;
    private ObjectMapper objectMapper = new GlobingularObjectMapperProvider().getContext(getClass());

    private String username;
    private String usernamePre = "hablebable";
    private static int usernameCount = 0;
    private Country c1, c2;
    private World world;
    private CountryCollector cc;
    private Response response;
    private String request, responseMsg;

    @BeforeAll
    public static void setUp() {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        target = c.target(Main.BASE_URI);
    }

    @BeforeEach
    public void beforeEach() {
        // Makes sure each call gets a new username
        username = usernamePre + (usernameCount++);
        c1 = new Country("NO", "Norway");
        c2 = new Country("SE", "Sweden");
        world = new World("testWorld", c1, c2);
        cc = new CountryCollector(world);
    }

    @AfterAll
    public static void tearDown() {
        server.shutdownNow();

        File folder = PersistenceHandler.DATA_FOLDER.toFile();
        for (File f : folder.listFiles()) {
                f.delete();
        }
        folder.delete();
    }

    /**
     * Test to see if a {@link CountryCollector} is sent back from the server.
     */
    @Test
    public void testGetCountryCollector() {

        response = target.path("globingular").path("countryCollector")
                .path(username).request().get();

        // 204 means No content, which is correct in this instance,
        // as the user "hablebable1" has no CountryCollector
        assertEquals(204, response.getStatus());
    }

    @Test
    public void testPutCountryCollector() throws JsonProcessingException {

        request = objectMapper.writeValueAsString(cc);

        response = target.path("globingular").path("countryCollector")
                .path(username).request().put(Entity.entity(request, MediaType.APPLICATION_JSON));

        // 200 means success - Request completed without errors
        assertEquals(200, response.getStatus());

        // Here we get a boolean value, so we check that it's true
        responseMsg = response.readEntity(String.class);
        assertEquals("true", responseMsg);
    }

    @Test
    public void testPutCountryCollectorFailsOnNull() throws JsonProcessingException {

        request = objectMapper.writeValueAsString(null);

        response = target.path("globingular").path("countryCollector")
                .path(username).request().put(Entity.entity(request, MediaType.APPLICATION_JSON));

        assertEquals(500, response.getStatus());
    }

    @Test
    public void testPutAndGetCountryCollector() throws JsonProcessingException {

        request = objectMapper.writeValueAsString(cc);

        response = target.path("globingular").path("countryCollector")
                .path(username).request().put(Entity.entity(request, MediaType.APPLICATION_JSON));

        // 200 means success - Request completed without errors
        assertEquals(200, response.getStatus());

        // Here we get a boolean value, so we check that it's true
        responseMsg = response.readEntity(String.class);
        assertEquals("true", responseMsg);

        response = target.path("globingular").path("countryCollector")
                .path(username).request().get();

        // 200 means success - We retrieved data without errors
        assertEquals(200, response.getStatus());

        // Check that the retrieved countryCollector has the right world
        responseMsg = response.readEntity(String.class);
        CountryCollector ccTemp = objectMapper.readValue(responseMsg, CountryCollector.class);
        assertEquals("testWorld", ccTemp.getWorld().getWorldName());
    }

    @Test
    public void testDeleteCountryCollector() {

        response = target.path("globingular").path("countryCollector")
                .path(username).request().delete();

        // 200 means Success - As there is nothing to delete for "hablebable4", it doesn't fail either
        assertEquals(200, response.getStatus());

        // Here we get a boolean value, so we check that it's true
        String responseMsg = response.readEntity(String.class);
        assertEquals("true", responseMsg);
    }

    @Test
    public void testPutAndDeleteAndGetCountryCollector() throws JsonProcessingException {

        request = objectMapper.writeValueAsString(cc);

        response = target.path("globingular").path("countryCollector")
                .path(username).request().put(Entity.entity(request, MediaType.APPLICATION_JSON));
        assertEquals(200, response.getStatus());
        responseMsg = response.readEntity(String.class);
        assertEquals("true", responseMsg);

        response = target.path("globingular").path("countryCollector")
                .path(username).request().delete();

        assertEquals(200, response.getStatus());
        responseMsg = response.readEntity(String.class);
        assertEquals("true", responseMsg);

        response = target.path("globingular").path("countryCollector")
                .path(username).request().get();

        assertEquals(204, response.getStatus());
    }

    @Test
    public void testRenameCountryCollectorForNewUser() {

        response = target.path("globingular").path("countryCollector")
                .path(username).path("rename").path(username + "Renamed").request().post(null);
        assertEquals(200, response.getStatus());
        responseMsg = response.readEntity(String.class);
        assertEquals("false", responseMsg);
    }
}
