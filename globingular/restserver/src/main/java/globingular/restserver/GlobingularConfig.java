package globingular.restserver;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import globingular.core.GlobingularModule;
import globingular.persistence.PersistenceHandler;
import globingular.restapi.GlobingularService;

/**
 * Jersey-configuration for the Globingular REST server.
 */
public class GlobingularConfig extends ResourceConfig {

    /**
     * The servers {@link GlobingularModule} instance, holding app-state.
     */
    private GlobingularModule globingularModule;

    /**
     * The {@link PersistenceHandler} to use for saving app-state.
     */
    private PersistenceHandler persistenceHandler;

    /**
     * Initialize config with REST API package.
     * 
     * @param globingularModule  The {@link GlobingularModule} to use for the server-instance.
     * @param persistenceHandler The {@link PersistenceHandler} to use for saving app-state.
     */
    public GlobingularConfig(final GlobingularModule globingularModule, final PersistenceHandler persistenceHandler) {
        this.globingularModule = globingularModule;
        this.persistenceHandler = persistenceHandler;
        register(GlobingularService.class);
        register(GlobingularObjectMapperProvider.class);
        register(JacksonFeature.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(GlobingularConfig.this.globingularModule);
                bind(GlobingularConfig.this.persistenceHandler);
            }
          });
    }

    /**
     * Initialize config with REST API package.
     * Using a new {@link PersistenceHandler} as none was given.
     * 
     * @param globingularModule The {@link GlobingularModule} to use for the server-instance.
     */
    public GlobingularConfig(final GlobingularModule globingularModule) {
        this(globingularModule, new PersistenceHandler());
    }

    /**
     * Initialize config with REST API package.
     * Using a new {@link GlobingularModule} as none was given.
     * Using a new {@link PersistenceHandler} as none was given.
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
        return this.globingularModule;
    }
}
