module globingular.ui {
	requires globingular.core;
	requires globingular.persistence;

	requires javafx.fxml;
	requires javafx.controls;
	requires javafx.swing;

	/* Batik-imports */
	requires batik.transcoder;
	requires batik.anim;
	requires batik.util;
	requires batik.dom;

	opens globingular.ui to javafx.fxml, javafx.graphics;
}
