package globingular.ui;

import com.fasterxml.jackson.databind.ObjectWriter;
import globingular.core.CountryCollector;
import globingular.persistence.PersistenceHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * {@link GlobingularDataAccess}-implementation using a REST-server.
 */
public class RestGlobingularDataAccess implements GlobingularDataAccess {

    /**
     * The name of the HTTP header defining accept-content-type.
     */
    public static final String ACCEPT_HEADER_NAME = "Accept";
    /**
     * The name of the HTTP header defining request body content-type.
     */
    public static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    /**
     * The MIME-type for JavaScript Object Notation (JSON).
     */
    public static final String MEDIA_TYPE_JSON = "application/json";
    /**
     * The HTTP status code signifying success but no content.
     */
    public static final int HTTP_STATUS_CODE_NO_CONTENT = 204;
    /**
     * The HTTP status code signifying success with content.
     */
    public static final int HTTP_STATUS_CODE_SUCCESS = 200;

    /**
     * The path to the Globingular-service, relative to the base-URI.
     */
    private static final String GLOBINGULAR_SERVICE_PATH = "globingular/";
    /**
     * The path to the CountryCollector-resource, relative to the service.
     */
    private static final String COUNTRY_COLLECTOR_RESOURCE_PATH = "countryCollector/";

    /**
     * The user to access the world-map data for.
     */
    private final String username;
    /**
     * The base URI of the REST server (including port).
     */
    private final String baseUri;
    /**
     * {@link PersistenceHandler} to use for parsing.
     */
    private final PersistenceHandler persistenceHandler;
    /**
     * Client used to connect to remote server.
     */
    private final HttpClient client = HttpClient.newBuilder().build();


    /**
     * Construct a new data access for the world-map of the given user.
     *
     * @param baseUri            The base URI of the REST server (including port).
     * @param username           The user to access the world-map data for.
     * @param persistenceHandler {@link PersistenceHandler} to use for parsing.
     */
    public RestGlobingularDataAccess(final String baseUri, final String username,
                                     final PersistenceHandler persistenceHandler) {
        this.baseUri = baseUri;
        this.username = username;
        this.persistenceHandler = persistenceHandler;
    }

    /**
     * {@inheritDoc}
     * Loads from a REST-API.
     */
    @Override
    public CountryCollector getCountryCollector() {
        try {
            final HttpRequest request =
                    HttpRequest.newBuilder(new URI(baseUri + GLOBINGULAR_SERVICE_PATH
                            + COUNTRY_COLLECTOR_RESOURCE_PATH + username))
                               .header(CONTENT_TYPE_HEADER_NAME, MEDIA_TYPE_JSON)
                               .header(ACCEPT_HEADER_NAME, MEDIA_TYPE_JSON)
                               .GET()
                               .build();


            final HttpResponse<InputStream> httpResponse =
                    client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // Check if the user didn't have anything stored. If so, return empty CountryCollector with default World
            if (httpResponse.statusCode() == HTTP_STATUS_CODE_NO_CONTENT) {
                // TODO: use field provided in future merge request
                return new CountryCollector(persistenceHandler.getDefaultWorld("Earth"));
            }

            return persistenceHandler.getObjectMapper().readValue(httpResponse.body(), CountryCollector.class);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            // Unexpected error, return empty CountryCollector with default World
            e.printStackTrace();
            // TODO: use field provided in future merge request
            return new CountryCollector(persistenceHandler.getDefaultWorld("Earth"));
        }
    }

    /**
     * {@inheritDoc}
     * Puts to a REST-API.
     */
    @Override
    public boolean putCountryCollector(final CountryCollector collector) {
        final ObjectWriter objectWriter = persistenceHandler.getObjectMapper().writerWithDefaultPrettyPrinter();

        try {
            final HttpRequest request =
                    HttpRequest.newBuilder(new URI(baseUri + GLOBINGULAR_SERVICE_PATH
                            + COUNTRY_COLLECTOR_RESOURCE_PATH + username))
                               .header(CONTENT_TYPE_HEADER_NAME, MEDIA_TYPE_JSON)
                               .header(ACCEPT_HEADER_NAME, MEDIA_TYPE_JSON)
                               .PUT(HttpRequest.BodyPublishers.ofString(objectWriter.writeValueAsString(collector)))
                               .build();

            final HttpResponse<InputStream> httpResponse =
                    client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (httpResponse.statusCode() == HTTP_STATUS_CODE_SUCCESS) {
                return persistenceHandler.getObjectMapper().readValue(httpResponse.body(), Boolean.class);
            } else {
                System.err.println("Failed to PUT countryCollector, received HTTP status code: "
                        + httpResponse.statusCode() + ". CountryCollector was:");
                System.err.println(collector);
                System.err.println("Username was: \"" + username + "\"");
                return false;
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }
}
