package globingular.restserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import globingular.core.Country;
import globingular.core.CountryCollector;
import globingular.core.Visit;
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
import java.time.LocalDateTime;

public class VisitResourceTest {

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

        System.out.println(username);
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

    @Test
    public void testRegisterVisit() throws JsonProcessingException {

        request = objectMapper.writeValueAsString(cc);

        response = target.path("globingular").path("countryCollector")
                .path(username).request().put(Entity.entity(request, MediaType.APPLICATION_JSON));

        // 200 means success - Request completed without errors
        assertEquals(200, response.getStatus());

        // Here we get a boolean value, so we check that it's true
        responseMsg = response.readEntity(String.class);
        assertEquals("true", responseMsg);

        // Visit with time values
        Visit v1 = new Visit(c1, LocalDateTime.now(), LocalDateTime.now());
        request = objectMapper.writeValueAsString(v1);

        response = target.path("globingular").path("countryCollector")
                .path(username).path("visit").path("register").request()
                .put(Entity.entity(request, MediaType.APPLICATION_JSON));

        // Success
        assertEquals(200, response.getStatus());

        // Check boolean return value
        responseMsg = response.readEntity(String.class);
        assertEquals("true", responseMsg);

        // Visit with null values
        Visit v2 = new Visit(c2, null, null);
        request = objectMapper.writeValueAsString(v2);

        response = target.path("globingular").path("countryCollector")
                .path(username).path("visit").path("register").request()
                .put(Entity.entity(request, MediaType.APPLICATION_JSON));

        // Success
        assertEquals(200, response.getStatus());

        // Check boolean return value
        responseMsg = response.readEntity(String.class);
        assertEquals("true", responseMsg);
    }

    @Test
    public void testRemoveVisit() throws JsonProcessingException {

        request = objectMapper.writeValueAsString(cc);

        response = target.path("globingular").path("countryCollector")
                .path(username).request().put(Entity.entity(request, MediaType.APPLICATION_JSON));

        // 200 means success - Request completed without errors
        assertEquals(200, response.getStatus());

        // Here we get a boolean value, so we check that it's true
        responseMsg = response.readEntity(String.class);
        assertEquals("true", responseMsg);

        // Visit with time values
        Visit v1 = new Visit(c1, LocalDateTime.now(), LocalDateTime.now());
        request = objectMapper.writeValueAsString(v1);

        response = target.path("globingular").path("countryCollector")
                .path(username).path("visit").path("register").request()
                .put(Entity.entity(request, MediaType.APPLICATION_JSON));

        // Success
        assertEquals(200, response.getStatus());

        // Check boolean return value
        responseMsg = response.readEntity(String.class);
        assertEquals("true", responseMsg);

        // Remove again
        response = target.path("globingular").path("countryCollector")
                .path(username).path("visit").path("remove").request()
                .put(Entity.entity(request, MediaType.APPLICATION_JSON));

        // Success
        assertEquals(200, response.getStatus());

        // Check boolean return value
        responseMsg = response.readEntity(String.class);
        assertEquals("true", responseMsg);
    }
}
