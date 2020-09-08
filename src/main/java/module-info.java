module globingular {
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires javafx.controls;
	
	exports globingular;

	opens globingular to javafx.fxml;
}