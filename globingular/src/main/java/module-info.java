module globingular {
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires javafx.base;
	
	requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.databind;
	requires java.desktop;
	requires batik.transcoder;
	requires javafx.swing;
	requires batik.anim;
	requires batik.util;

	opens globingular.ui to javafx.fxml, javafx.graphics;
}