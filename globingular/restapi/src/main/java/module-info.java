module globingular.restapi {
    requires jakarta.ws.rs;
    requires jakarta.inject;
    requires transitive globingular.persistence;

    exports globingular.restapi;
}
