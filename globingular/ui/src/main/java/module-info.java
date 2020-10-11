module globingular.ui {
    requires transitive globingular.core;

	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires javafx.base;
	
	requires java.desktop;
	requires javafx.swing;

	/* Batik-imports */
	requires batik.transcoder;
	requires batik.anim;
	requires batik.util;
	requires batik.dom;

	opens globingular.ui to javafx.fxml, javafx.graphics;
}
