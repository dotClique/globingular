package globingular.ui;

import com.fasterxml.jackson.databind.ObjectWriter;
import globingular.core.CountryCollector;
import globingular.core.Visit;
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
     * The endpoint for renaming a CountryCollector-resource. must be appended AFTER username.
     * Includes a preceeding slash, as there will be a username inserted after CountryCollector-resource.
     */
    private static final String COUNTRY_COLLECTOR_RESOURCE_ACTION_RENAME = "/rename/";
    /**
     * The path to the Visit-resource, relative to the CountryCollector-resource (including username).
     * Includes a preceeding slash, as there will be a username inserted after CountryCollector-resource.
     */
    private static final String VISIT_RESOURCE_PATH = "/visit/";
    /**
     * The endpoint for registering a {@link Visit}.
     */
    private static final String VISIT_RESOURCE_PATH_ACTION_REGISTER = VISIT_RESOURCE_PATH + "register";
    /**
     * The endpoint for removing a {@link Visit}.
     */
    private static final String VISIT_RESOURCE_PATH_ACTION_REMOVE = VISIT_RESOURCE_PATH + "remove";

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
     * {@inheritDoc} Loads from a REST-API.
     */
    @Override
    public CountryCollector getCountryCollector() {
        try {
            final HttpRequest request = HttpRequest
                    .newBuilder(
                            new URI(baseUri + GLOBINGULAR_SERVICE_PATH + COUNTRY_COLLECTOR_RESOURCE_PATH + username))
                    .header(CONTENT_TYPE_HEADER_NAME, MEDIA_TYPE_JSON).header(ACCEPT_HEADER_NAME, MEDIA_TYPE_JSON).GET()
                    .build();

            final HttpResponse<InputStream> httpResponse = client.send(request,
                    HttpResponse.BodyHandlers.ofInputStream());

            // If not NO_CONTENT, return it. If NO_CONTENT, return null.
            if (httpResponse.statusCode() != HTTP_STATUS_CODE_NO_CONTENT) {
                return persistenceHandler.getObjectMapper().readValue(httpResponse.body(), CountryCollector.class);
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            // Unexpected error. Print stack trace and return null.
            e.printStackTrace();
        }
        // No data retrieved from the server. The caller needs to create a new instance and save it.
        return null;
    }

    /**
     * {@inheritDoc} Saves to a REST-API.
     */
    @Override
    public boolean saveCountryCollector(final CountryCollector collector) {
        final ObjectWriter objectWriter = persistenceHandler.getObjectMapper().writerWithDefaultPrettyPrinter();

        try {
            final HttpRequest request = HttpRequest
                    .newBuilder(
                            new URI(baseUri + GLOBINGULAR_SERVICE_PATH + COUNTRY_COLLECTOR_RESOURCE_PATH + username))
                    .header(CONTENT_TYPE_HEADER_NAME, MEDIA_TYPE_JSON).header(ACCEPT_HEADER_NAME, MEDIA_TYPE_JSON)
                    .PUT(HttpRequest.BodyPublishers.ofString(objectWriter.writeValueAsString(collector))).build();

            final HttpResponse<InputStream> httpResponse = client.send(request,
                    HttpResponse.BodyHandlers.ofInputStream());
            if (httpResponse.statusCode() == HTTP_STATUS_CODE_SUCCESS) {
                return persistenceHandler.getObjectMapper().readValue(httpResponse.body(), Boolean.class);
            } else {
                System.err.println("Failed to PUT countryCollector, received HTTP status code: "
                        + httpResponse.statusCode() + " from " + httpResponse.uri().toString()
                        + ". CountryCollector was:");
                System.err.println(collector);
                System.err.println("Username was: \"" + username + "\"");
                return false;
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc} Saves to a REST-API.
     */
    public boolean renameCountryCollector(final String newUsername, final CountryCollector collector) {
        try {
            final HttpRequest request = HttpRequest
                    .newBuilder(
                            new URI(baseUri + GLOBINGULAR_SERVICE_PATH + COUNTRY_COLLECTOR_RESOURCE_PATH
                            + username + COUNTRY_COLLECTOR_RESOURCE_ACTION_RENAME + newUsername))
                    .header(ACCEPT_HEADER_NAME, MEDIA_TYPE_JSON).POST(null).build();

            final HttpResponse<InputStream> httpResponse = client.send(request,
                    HttpResponse.BodyHandlers.ofInputStream());
            if (httpResponse.statusCode() == HTTP_STATUS_CODE_SUCCESS) {
                return persistenceHandler.getObjectMapper().readValue(httpResponse.body(), Boolean.class);
            } else {
                System.err.println("Failed to POST countryCollector-rename, received HTTP status code: "
                        + httpResponse.statusCode() + " from " + httpResponse.uri().toString()
                        + ". CountryCollector was:");
                System.err.println("Username was: \"" + username + "\"");
                System.err.println("New username was: \"" + newUsername + "\"");
                return false;
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc} Deletes from a REST-API.
     */
    @Override
    public boolean deleteCountryCollector() {
        try {
            final HttpRequest request = HttpRequest
                    .newBuilder(
                            new URI(baseUri + GLOBINGULAR_SERVICE_PATH + COUNTRY_COLLECTOR_RESOURCE_PATH + username))
                    .header(ACCEPT_HEADER_NAME, MEDIA_TYPE_JSON).DELETE().build();

            final HttpResponse<InputStream> httpResponse = client.send(request,
                    HttpResponse.BodyHandlers.ofInputStream());
            if (httpResponse.statusCode() == HTTP_STATUS_CODE_SUCCESS) {
                return persistenceHandler.getObjectMapper().readValue(httpResponse.body(), Boolean.class);
            } else {
                System.err.println("Failed to DELETE countryCollector, received HTTP status code: "
                        + httpResponse.statusCode() + " from " + httpResponse.uri().toString() + ".");
                System.err.println("Username was: \"" + username + "\"");
                return false;
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc} Saves to a REST-API.
     */
    @Override
    public boolean saveVisit(final CountryCollector collector, final Visit visit) {
        final ObjectWriter objectWriter = persistenceHandler.getObjectMapper().writerWithDefaultPrettyPrinter();

        try {
            final HttpRequest request = HttpRequest
                    .newBuilder(
                            new URI(baseUri + GLOBINGULAR_SERVICE_PATH + COUNTRY_COLLECTOR_RESOURCE_PATH
                            + username + VISIT_RESOURCE_PATH_ACTION_REGISTER))
                    .header(CONTENT_TYPE_HEADER_NAME, MEDIA_TYPE_JSON).header(ACCEPT_HEADER_NAME, MEDIA_TYPE_JSON)
                    .PUT(HttpRequest.BodyPublishers.ofString(objectWriter.writeValueAsString(visit))).build();

            final HttpResponse<InputStream> httpResponse = client.send(request,
                    HttpResponse.BodyHandlers.ofInputStream());
            if (httpResponse.statusCode() == HTTP_STATUS_CODE_SUCCESS) {
                return persistenceHandler.getObjectMapper().readValue(httpResponse.body(), Boolean.class);
            } else {
                System.err.println("Failed to PUT visit, received HTTP status code: "
                        + httpResponse.statusCode() + " from " + httpResponse.uri().toString() + ". Visit was:");
                System.err.println(visit);
                System.err.println("Username was: \"" + username + "\"");
                return false;
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc} Deletes from a REST-API.
     */
    @Override
    public boolean deleteVisit(final CountryCollector collector, final Visit visit) {
        final ObjectWriter objectWriter = persistenceHandler.getObjectMapper().writerWithDefaultPrettyPrinter();

        try {
            final HttpRequest request = HttpRequest
                    .newBuilder(
                            new URI(baseUri + GLOBINGULAR_SERVICE_PATH + COUNTRY_COLLECTOR_RESOURCE_PATH
                            + username + VISIT_RESOURCE_PATH_ACTION_REMOVE))
                    .header(CONTENT_TYPE_HEADER_NAME, MEDIA_TYPE_JSON).header(ACCEPT_HEADER_NAME, MEDIA_TYPE_JSON)
                    .PUT(HttpRequest.BodyPublishers.ofString(objectWriter.writeValueAsString(visit))).build();

            final HttpResponse<InputStream> httpResponse = client.send(request,
                    HttpResponse.BodyHandlers.ofInputStream());
            if (httpResponse.statusCode() == HTTP_STATUS_CODE_SUCCESS) {
                return persistenceHandler.getObjectMapper().readValue(httpResponse.body(), Boolean.class);
            } else {
                System.err.println("Failed to PUT visit, received HTTP status code: "
                        + httpResponse.statusCode() + " from " + httpResponse.uri().toString() + ". Visit was:");
                System.err.println(visit);
                System.err.println("Username was: \"" + username + "\"");
                return false;
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }
}
