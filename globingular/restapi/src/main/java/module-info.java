module globingular.restapi {
    requires jakarta.ws.rs;
    requires jakarta.inject;
    requires transitive globingular.core;

    exports globingular.restapi;
    opens globingular.restapi;
}
