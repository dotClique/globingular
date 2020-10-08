module globingular.persistence {
    requires globingular.core;

	requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.databind;

	exports globingular.persistence;
}
