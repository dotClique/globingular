module it1901.gr2002.globingular.core {
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires javafx.base;
	
	requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.databind;
	requires java.desktop;
	requires javafx.swing;

	/* Batik-imports */
	requires batik.transcoder;
	requires batik.anim;
	requires batik.util;
	requires batik.dom;

	exports it1901.gr2002.globingular.core;
}
