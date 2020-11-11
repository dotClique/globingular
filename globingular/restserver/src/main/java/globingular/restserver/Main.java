package globingular.restserver;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 */
public final class Main {

    private Main() {
    }

    /**
     * Base URI the Grizzly HTTP server will listen on.
     */
    public static final String BASE_URI = "http://localhost:8081/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     *
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in REST API package
        final ResourceConfig rc = new GlobingularConfig();

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method. Starts server, and shut downs on Enter-keypress.
     *
     * @param args Ignored
     * @throws IOException On failure to read from System.in
     */
    public static void main(final String[] args) throws IOException {
        final HttpServer server = startServer();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        System.out.printf("Jersey app started with WADL available at "
                + "%sapplication.wadl%nHit enter to stop it...%n", BASE_URI);
        System.in.read();
        server.shutdownNow();
    }
}

