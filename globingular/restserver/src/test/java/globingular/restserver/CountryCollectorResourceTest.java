package globingular.restserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import globingular.core.Country;
import globingular.core.CountryCollector;
import globingular.core.Visit;
import globingular.core.World;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

public class CountryCollectorResourceTest {

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

    /**
     * Test to see if a {@link CountryCollector} is sent back from the server.
     */
    @Test
    public void testGetCountryCollector() {
        String username = "hablebable1";

        Response response = target.path("globingular").path("countryCollector")
                .path(username).request().get();

        // 204 means No content, which is correct in this instance,
        // as the user "hablebable1" has no CountryCollector
        assertEquals(204, response.getStatus());
    }

    @Test
    public void testPutCountryCollector() throws JsonProcessingException {
        String username = "hablebable2";

        Country c1 = new Country("NO", "Norway");
        Country c2 = new Country("SE", "Sweden");
        World world = new World("testWorld", c1, c2);
        CountryCollector cc = new CountryCollector(world);
        Response response;
        String request, responseMsg;

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
    public void testPutAndGetCountryCollector() throws JsonProcessingException {
        String username = "hablebable3";

        Country c1 = new Country("NO", "Norway");
        Country c2 = new Country("SE", "Sweden");
        World world = new World("testWorld", c1, c2);
        CountryCollector cc = new CountryCollector(world);
        Response response;
        String request, responseMsg;

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
        String username = "hablebable4";

        Response response = target.path("globingular").path("countryCollector")
                .path(username).request().delete();

        // 200 means Success - As there is nothing to delete for "hablebable4", it doesn't fail either
        assertEquals(200, response.getStatus());

        // Here we get a boolean value, so we check that it's true
        String responseMsg = response.readEntity(String.class);
        assertEquals("true", responseMsg);
    }

    @Test
    public void testPutAndDeleteAndGetCountryCollector() throws JsonProcessingException {
        String username = "hablebable5";

        Country c1 = new Country("NO", "Norway");
        Country c2 = new Country("SE", "Sweden");
        World world = new World("testWorld", c1, c2);
        CountryCollector cc = new CountryCollector(world);
        Response response;
        String request, responseMsg;

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
    public void testRegisterVisit() throws JsonProcessingException {
        String username = "hablebable6";

        Country c1 = new Country("NO", "Norway");
        Country c2 = new Country("SE", "Sweden");
        World world = new World("testWorld", c1, c2);
        CountryCollector cc = new CountryCollector(world);

        String s1 = objectMapper.writeValueAsString(cc);

        Response response1 = target.path("globingular").path("countryCollector")
                .path(username).request().put(Entity.entity(s1, MediaType.APPLICATION_JSON));

        // 200 means success - Request completed without errors
        assertEquals(200, response1.getStatus());

        // Here we get a boolean value, so we check that it's true
        String responseMsg1 = response1.readEntity(String.class);
        assertEquals("true", responseMsg1);

        // Visit with time values
        Visit v1 = new Visit(c1, LocalDateTime.now(), LocalDateTime.now());
        String s2 = objectMapper.writeValueAsString(v1);

        Response response2 = target.path("globingular").path("countryCollector")
                .path(username).path("visit").path("register").request()
                .put(Entity.entity(s2, MediaType.APPLICATION_JSON));

        // Success
        assertEquals(200, response2.getStatus());

        // Check boolean return value
        String responseMsg2 = response2.readEntity(String.class);
        assertEquals("true", responseMsg2);

        // Visit with null values
        Visit v2 = new Visit(c2, null, null);
        String s3 = objectMapper.writeValueAsString(v2);

        Response response3 = target.path("globingular").path("countryCollector")
                .path(username).path("visit").path("register").request()
                .put(Entity.entity(s3, MediaType.APPLICATION_JSON));

        // Success
        assertEquals(200, response3.getStatus());

        // Check boolean return value
        String responseMsg3 = response3.readEntity(String.class);
        assertEquals("true", responseMsg3);
    }

    @Test
    public void testRemoveVisit() throws JsonProcessingException {
        String username = "hablebable7";

        Country c1 = new Country("NO", "Norway");
        Country c2 = new Country("SE", "Sweden");
        World world = new World("testWorld", c1, c2);
        CountryCollector cc = new CountryCollector(world);

        String s1 = objectMapper.writeValueAsString(cc);

        Response response1 = target.path("globingular").path("countryCollector")
                .path(username).request().put(Entity.entity(s1, MediaType.APPLICATION_JSON));

        // 200 means success - Request completed without errors
        assertEquals(200, response1.getStatus());

        // Here we get a boolean value, so we check that it's true
        String responseMsg1 = response1.readEntity(String.class);
        assertEquals("true", responseMsg1);

        // Visit with time values
        Visit v1 = new Visit(c1, LocalDateTime.now(), LocalDateTime.now());
        String s2 = objectMapper.writeValueAsString(v1);

        Response response2 = target.path("globingular").path("countryCollector")
                .path(username).path("visit").path("register").request()
                .put(Entity.entity(s2, MediaType.APPLICATION_JSON));

        // Success
        assertEquals(200, response2.getStatus());

        // Check boolean return value
        String responseMsg2 = response2.readEntity(String.class);
        assertEquals("true", responseMsg2);

        // Remove again
        Response response3 = target.path("globingular").path("countryCollector")
                .path(username).path("visit").path("remove").request()
                .put(Entity.entity(s2, MediaType.APPLICATION_JSON));

        // Success
        assertEquals(200, response3.getStatus());

        // Check boolean return value
        String responseMsg3 = response3.readEntity(String.class);
        assertEquals("false", responseMsg3);
    }

    @Test
    public void testRenameCountryCollector() {
            // TODO: Not implemented!
    }
}
