package globingular.restserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import globingular.persistence.PersistenceHandler;

import jakarta.ws.rs.ext.ContextResolver;

/**
 * Provides custom {@link ObjectMapper} for use in (de)serialization of Globingular classes.
 */
public class GlobingularObjectMapperProvider implements ContextResolver<ObjectMapper> {
    /**
     * {@link ObjectMapper} to return.
     */
    private final ObjectMapper objectMapper = new PersistenceHandler().getObjectMapper();

    /**
     * Custom {@link ObjectMapper} for use in (de)serialization of Globingular classes.
     * @param aClass Ignored
     */
    @Override
    public ObjectMapper getContext(final Class<?> aClass) {
        return objectMapper;
    }
}
