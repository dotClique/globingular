module globingular {
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

	opens globingular.ui to javafx.fxml, javafx.graphics;
	opens globingular.core to com.fasterxml.jackson.databind;
}
