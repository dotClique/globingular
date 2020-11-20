package globingular.ui;

import globingular.core.CountryCollector;
import globingular.core.Visit;
import globingular.persistence.PersistenceHandler;

import java.io.IOException;
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
     * HTTP method for deleting.
     */
    public static final String HTTP_METHOD_DELETE = "DELETE";

    /**
     * HTTP method for getting.
     */
    public static final String HTTP_METHOD_GET = "GET";

    /**
     * HTTP method for putting.
     */
    public static final String HTTP_METHOD_PUT = "PUT";

    /**
     * HTTP method for posting.
     */
    public static final String HTTP_METHOD_POST = "POST";

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
        final String uri = baseUri + GLOBINGULAR_SERVICE_PATH + COUNTRY_COLLECTOR_RESOURCE_PATH + username;
        return executeRequest(HTTP_METHOD_GET, uri, null, CountryCollector.class, null, HTTP_STATUS_CODE_SUCCESS);
    }

    /**
     * {@inheritDoc} Saves to a REST-API.
     */
    @Override
    public boolean saveCountryCollector(final CountryCollector collector) {
        final String uri = baseUri + GLOBINGULAR_SERVICE_PATH + COUNTRY_COLLECTOR_RESOURCE_PATH + username;
        return executeRequest(HTTP_METHOD_PUT, uri, collector);
    }

    /**
     * {@inheritDoc} Saves to a REST-API.
     */
    public boolean renameCountryCollector(final String newUsername, final CountryCollector collector) {
        final String uri = baseUri + GLOBINGULAR_SERVICE_PATH + COUNTRY_COLLECTOR_RESOURCE_PATH
                + username + COUNTRY_COLLECTOR_RESOURCE_ACTION_RENAME + newUsername;
        return executeRequest(HTTP_METHOD_POST, uri, null);
    }

    /**
     * {@inheritDoc} Deletes from a REST-API.
     */
    @Override
    public boolean deleteCountryCollector() {
        final String uri = baseUri + GLOBINGULAR_SERVICE_PATH + COUNTRY_COLLECTOR_RESOURCE_PATH + username;
        return executeRequest(HTTP_METHOD_DELETE, uri, null);
    }

    /**
     * {@inheritDoc} Saves to a REST-API.
     */
    @Override
    public boolean saveVisit(final CountryCollector collector, final Visit visit) {
        final String uri = baseUri + GLOBINGULAR_SERVICE_PATH + COUNTRY_COLLECTOR_RESOURCE_PATH
                + username + VISIT_RESOURCE_PATH_ACTION_REGISTER;
        return executeRequest(HTTP_METHOD_POST, uri, visit);
    }

    /**
     * {@inheritDoc} Deletes from a REST-API.
     */
    @Override
    public boolean deleteVisit(final CountryCollector collector, final Visit visit) {
        final String uri = baseUri + GLOBINGULAR_SERVICE_PATH + COUNTRY_COLLECTOR_RESOURCE_PATH
                + username + VISIT_RESOURCE_PATH_ACTION_REMOVE;
        return executeRequest(HTTP_METHOD_POST, uri, visit);
    }


    /**
     * Performs a network-request with the given parameters.
     * Uses {@code false} for defaultReturn and sets HTTP_STATUS_CODE_SUCCESS as accaptable return.
     * 
     * @param method                The method-type to call.
     * @param uri                   The URI to access.
     * @param parameter             A parameter to pass along to server.
     *                              Only used for PUT and POST requests.
     * @return                      Parsed boolean response from server,
     *                              or default: false if the given requirements are not met.
     */
    private Boolean executeRequest(final String method, final String uri, final Object parameter) {
        return executeRequest(method, uri, parameter, Boolean.class, false, HTTP_STATUS_CODE_SUCCESS);
    }
    /**
     * Performs a network-request with the given parameters.
     * 
     * @param <T>                   The type to return.
     * @param method                The method-type to call.
     * @param uri                   The URI to access.
     * @param parameter             A parameter to pass along to server.
     *                              Only used for PUT and POST requests.
     * @param returnType            The type to return.
     * @param defaultReturn         Default return value.
     *                              Returned if exceptions or non-acceptable status codes.
     * @param acceptableStatusCodes Http status codes to accept.
     *                              Returns defaultReturn if these status codes are not met.
     * @return                      A parsed version of the response,
     *                              or defaultReturn if the given requirements are not met.
     */
    private <T> T executeRequest(final String method, final String uri, final Object parameter,
            final Class<T> returnType, final T defaultReturn, final int... acceptableStatusCodes) {
        try {
            final HttpRequest request;
            switch (method) {
                case HTTP_METHOD_GET:
                    request = HttpRequest.newBuilder(new URI(uri)).header(ACCEPT_HEADER_NAME, MEDIA_TYPE_JSON).GET()
                            .build();
                    break;
                case HTTP_METHOD_POST:
                    request = HttpRequest.newBuilder(new URI(uri)).header(CONTENT_TYPE_HEADER_NAME, MEDIA_TYPE_JSON)
                            .header(ACCEPT_HEADER_NAME, MEDIA_TYPE_JSON).POST(HttpRequest.BodyPublishers
                            .ofString(persistenceHandler.serialize(parameter))).build();
                    break;
                case HTTP_METHOD_PUT:
                    request = HttpRequest.newBuilder(new URI(uri)).header(CONTENT_TYPE_HEADER_NAME, MEDIA_TYPE_JSON)
                            .header(ACCEPT_HEADER_NAME, MEDIA_TYPE_JSON).PUT(HttpRequest.BodyPublishers
                            .ofString(persistenceHandler.serialize(parameter))).build();
                    break;
                case HTTP_METHOD_DELETE:
                    request = HttpRequest.newBuilder(new URI(uri)).header(ACCEPT_HEADER_NAME, MEDIA_TYPE_JSON)
                            .DELETE().build();
                    break;
                default:
                    request = null;
                    break;
            }
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            for (int statusCode : acceptableStatusCodes) {
                if (response.statusCode() == statusCode) {
                    return persistenceHandler.parse(response.body(), returnType);
                }
            }
            System.err.println("Network " + method + " request failed, received HTTP status code: "
                    + response.statusCode() + " from " + response.uri().toString() + ".");
            if (parameter != null) {
                System.err.println("Parameter was:");
                System.err.println(parameter);
            }
            if (username != null) {
                System.err.println("Username was: \"" + username + "\"");
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
        return defaultReturn;
    }
}
