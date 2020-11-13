module globingular.restapi {
    requires jakarta.ws.rs;
    requires jakarta.inject;
    requires transitive globingular.core;

    exports globingular.restapi;

    // Required for the restserver to inject values
    opens globingular.restapi;
}
