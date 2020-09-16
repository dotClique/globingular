module globingular {
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires java.desktop;
	requires batik.transcoder;
	requires batik.bridge;
	requires batik.dom;
	requires batik.css;
	requires batik.gvt;
	requires batik.util;
	requires batik.anim;
	requires batik.i18n;
	requires batik.svg.dom;
	requires batik.xml;
	requires batik.constants;
	requires batik.awt.util;
	requires batik.ext;
	requires batik.parser;
	requires batik.script;
	requires batik.svggen;
	requires xml.apis.ext;
	requires xmlgraphics.commons;
	requires jdk.xml.dom;
	requires javafx.swing;

	uses org.apache.batik.bridge.RhinoInterpreterFactory;

	// provides org.apache.batik.script.InterpreterFactory with org.apache.batik.bridge.RhinoInterpreterFactory;

	exports globingular;

	opens globingular to javafx.fxml;
}
