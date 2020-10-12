module globingular.ui {
	requires globingular.core;
	requires globingular.persistence;

	requires javafx.fxml;
	requires javafx.web;
	
	requires java.desktop;

	opens globingular.ui to javafx.fxml, javafx.graphics;
}
