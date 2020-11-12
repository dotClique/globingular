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

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountryCollectorResourceTest {

    private static HttpServer server;
    private static WebTarget target;
    PersistenceHandler persistenceHandler = new PersistenceHandler();
    ObjectMapper objectMapper = persistenceHandler.getObjectMapper();

    @BeforeAll
    public static void setUp() {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new
        // org.glassfish.jersey.media.json.JsonJaxbFeature());

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
        String responseMsg = target.path("globingular").path("countryCollector")
                .path("hablebable1").request().get(String.class);
        CountryCollector countryCollector = objectMapper.readValue(responseMsg, CountryCollector.class);
        assertNull(countryCollector.getWorld().getWorldName());
    }

    @Test
    public void testPutCountryCollector() throws JsonProcessingException {
        Country c1 = new Country("NO", "Norway");
        Country c2 = new Country("SE", "Sweden");
        World world = new World("testWorld", c1, c2);
        CountryCollector cc = new CountryCollector(world);

        String s1 = objectMapper.writeValueAsString(cc);

        String responseMsg = target.path("globingular").path("countryCollector")
                .path("hablebable2").request().put(Entity.entity(s1, MediaType.APPLICATION_JSON), String.class);

        assertEquals("true", responseMsg);
    }

    @Test
    public void testPutAndGetCountryCollector() throws JsonProcessingException {
        Country c1 = new Country("NO", "Norway");
        Country c2 = new Country("SE", "Sweden");
        World world = new World("testWorld", c1, c2);
        CountryCollector cc = new CountryCollector(world);

        String s1 = objectMapper.writeValueAsString(cc);

        String responseMsg = target.path("globingular").path("countryCollector")
                .path("hablebable3").request().put(Entity.entity(s1, MediaType.APPLICATION_JSON), String.class);

        assertEquals("true", responseMsg);

        String responseMsg2 = target.path("globingular").path("countryCollector")
                .path("hablebable3").request().get(String.class);

        CountryCollector countryCollector = objectMapper.readValue(responseMsg2, CountryCollector.class);
        assertEquals("testWorld", countryCollector.getWorld().getWorldName());
    }
}
