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
     *                           Will use a default instance if null.
     * @param persistenceHandler The {@link PersistenceHandler} to use for saving app-state.
     *                           Will use a default instance if null.
     */
    public GlobingularConfig(final GlobingularModule globingularModule, final PersistenceHandler persistenceHandler) {
        this.globingularModule = (globingularModule == null ? createDefaultGlobingularModule() : globingularModule);
        this.persistenceHandler = (persistenceHandler == null ? createDefaultPersistenceHandler() : persistenceHandler);
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
     * Using a new {@link PersistenceHandler}-instance as none was given.
     * 
     * @param globingularModule The {@link GlobingularModule} to use for the server-instance.
     */
    public GlobingularConfig(final GlobingularModule globingularModule) {
        this(globingularModule, null);
    }

    /**
     * Initialize config with REST API package.
     * Using a new {@link GlobingularModule}-instance as none was given.
     * 
     * @param persistenceHandler The {@link PersistenceHandler} to use for saving app-state.
     */
    public GlobingularConfig(final PersistenceHandler persistenceHandler) {
        this(null, persistenceHandler);
    }

    /**
     * Initialize config with REST API package.
     * Using new {@link GlobingularModule}- and {@link PersistenceHandler}-instances as none was given.
     */
    public GlobingularConfig() {
        this(null, null);
    }

    /**
     * Get this instance's {@link GlobingularModule}.
     * 
     * @return This instance's {@link GlobingularModule}
     */
    public GlobingularModule getGlobingularModule() {
        return this.globingularModule;
    }
    // TODO: Should we have a setGlobingularModule?

    private GlobingularModule createDefaultGlobingularModule() {
        return new GlobingularModule();
    }

    // TODO: Should default be null to disable saving of state?
    private PersistenceHandler createDefaultPersistenceHandler() {
        return new PersistenceHandler();
    }
}
