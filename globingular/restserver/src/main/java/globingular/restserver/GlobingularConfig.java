package globingular.restserver;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import globingular.core.GlobingularModule;

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
     * The servers {@link GlobingularModule} instance, holding app-state.
     */
    private GlobingularModule globingularModule;

    /**
     * Initialize config with REST API package.
     * 
     * @param globingularModule The {@link GlobingularModule} to use for the server-instance.
     */
    public GlobingularConfig(final GlobingularModule globingularModule) {
        setGlobingularModule(globingularModule);
        packages(REST_API_PACKAGE);
        register(GlobingularObjectMapperProvider.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
              bind(GlobingularConfig.this.globingularModule);
            }
          });
    }

    /**
     * Initialize config with REST API package.
     */
    public GlobingularConfig() {
        this(new GlobingularModule());
    }

    /**
     * Get this instance's {@link GlobingularModule}.
     * 
     * @return This instance's {@link GlobingularModule}
     */
    public GlobingularModule getGlobingularModule() {
        return this.globingularModule; // TODO: Should this be protected, or is it ment to return it?
    }

    private void setGlobingularModule(final GlobingularModule globingularModule) {
        this.globingularModule = globingularModule;
    }
}
