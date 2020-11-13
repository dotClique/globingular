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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountryCollectorResourceTest {

    private static HttpServer server;
    private static WebTarget target;
    PersistenceHandler persistenceHandler = new PersistenceHandler();
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
    public void testGetCountryCollector() throws JsonProcessingException {
        Response response = target.path("globingular").path("countryCollector")
                .path("hablebable1").request().get();

        // 204 means No Content, which is correct in this instance,
        // as there is no CountryCollector for user "hablebable1"
        assertEquals(204, response.getStatus());

        String responseMsg = response.readEntity(String.class);
        assertEquals("", responseMsg);
    }

    @Test
    public void testPutCountryCollector() throws JsonProcessingException {
        Country c1 = new Country("NO", "Norway");
        Country c2 = new Country("SE", "Sweden");
        World world = new World("testWorld", c1, c2);
        CountryCollector cc = new CountryCollector(world);

        String s1 = objectMapper.writeValueAsString(cc);

        Response response = target.path("globingular").path("countryCollector")
                .path("hablebable2").request().put(Entity.entity(s1, MediaType.APPLICATION_JSON));

        assertEquals(200, response.getStatus());

        String responseMsg = response.readEntity(String.class);
        assertEquals("true", responseMsg);
    }

    @Test
    public void testPutAndGetCountryCollector() throws JsonProcessingException {
        Country c1 = new Country("NO", "Norway");
        Country c2 = new Country("SE", "Sweden");
        World world = new World("testWorld", c1, c2);
        CountryCollector cc = new CountryCollector(world);

        String s1 = objectMapper.writeValueAsString(cc);

        Response response1 = target.path("globingular").path("countryCollector")
                .path("hablebable3").request().put(Entity.entity(s1, MediaType.APPLICATION_JSON));
        assertEquals(200, response1.getStatus());
        String responseMsg1 = response1.readEntity(String.class);
        assertEquals("true", responseMsg1);

        Response response2 = target.path("globingular").path("countryCollector")
                .path("hablebable3").request().get();

        assertEquals(200, response2.getStatus());

        String responseMsg2 = response2.readEntity(String.class);
        CountryCollector countryCollector = objectMapper.readValue(responseMsg2, CountryCollector.class);
        assertEquals("testWorld", countryCollector.getWorld().getWorldName());
    }
}
