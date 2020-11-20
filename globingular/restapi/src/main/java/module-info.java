module globingular.restapi {
    requires jakarta.ws.rs;
    requires jakarta.inject;
    requires transitive globingular.persistence;
    requires org.slf4j;

    exports globingular.restapi;
}
