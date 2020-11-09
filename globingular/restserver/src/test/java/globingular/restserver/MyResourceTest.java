package globingular.restserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import globingular.core.Country;
import globingular.core.CountryCollector;
import globingular.core.World;
import globingular.persistence.PersistenceHandler;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MyResourceTest {

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
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Main.BASE_URI);
    }

    @AfterAll
    public static void tearDown() {
        server.shutdownNow();
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() throws JsonProcessingException {
        String responseMsg = target.path("countryCollector").request().get(String.class);
        assertNotNull(objectMapper);
        assertNotNull(responseMsg);
        assertNotNull(Country.class);
        Country c = objectMapper.readValue(responseMsg, Country.class);
        assertEquals("Country{countryCode='NO', shortName='Norway', longName='Norway', sovereignty='UN', region='', population=0, provinces=[]}", c.toString());
    }

    @Test
    public void testGetOne() throws JsonProcessingException {
        String responseMsg = target.path("countryCollector").path("hablebable").request().get(String.class);
        World world = new World(new Country("NO", "Norway"));
        CountryCollector countryCollector = objectMapper.readValue(responseMsg, CountryCollector.class);
        assertEquals("[[Norway at 2020-11-02T22:22:28.792700100 - 2020-11-03T22:22:28.792700100]]",
                countryCollector.toString());
    }
}
