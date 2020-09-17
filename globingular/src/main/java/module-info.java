module globingular {
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires javafx.controls;
	
	opens globingular.ui to javafx.fxml;
}