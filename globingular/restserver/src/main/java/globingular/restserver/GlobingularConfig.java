package globingular.restserver;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Jersey-configuration for the Globingular REST server.
 */
public class GlobingularConfig extends ResourceConfig {

    /**
     * REST API package to make available.
     *
     * @see globingular.restapi
     */
    public static final String REST_API_PACKAGE = "globingular.restapi";

    /**
     * Initialize config with REST API package.
     */
    public GlobingularConfig() {
        packages(REST_API_PACKAGE);
    }
}
