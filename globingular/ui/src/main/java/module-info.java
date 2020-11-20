module globingular.ui {
	requires globingular.core;
	requires globingular.persistence;

	requires javafx.fxml;
	requires javafx.web;

	requires org.controlsfx.controls;
	
	requires java.desktop;
	requires java.net.http;

	opens globingular.ui to javafx.fxml, javafx.graphics;
}
