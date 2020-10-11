module globingular.core {
	requires javafx.base;
	//requires globingular.persistence;

	requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.databind;
	
	exports globingular.core;
}
