module globingular {
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires java.desktop;
	requires batik.transcoder;
	requires batik.bridge;
	requires batik.dom;
	requires javafx.swing;

	exports globingular;

	opens globingular to javafx.fxml;
}