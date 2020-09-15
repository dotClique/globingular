module globingular {
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires java.desktop;
	requires batik.transcoder;
	requires batik.bridge;
	requires javafx.swing;

	exports globingular;

	opens globingular to javafx.fxml, batik.bridge, batik.transcoder;

}