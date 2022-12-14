module globingular.restserver {
    // Grizzly web-server
    requires grizzly.http.server;
    // Prevents compilation failure: cannot access org.glassfish.grizzly.GrizzlyFuture
    requires grizzly.framework;

    requires com.fasterxml.jackson.databind;

    // JAX-RS Jersey REST framework
    requires jersey.server;
    requires jersey.container.grizzly2.http;
    requires jersey.media.json.jackson;
    // Prevents compilation failure: cannot access org.glassfish.jersey.ExtendedConfig
    requires jersey.common;

    requires transitive globingular.persistence;
    requires globingular.restapi;
    requires jakarta.ws.rs;
}
