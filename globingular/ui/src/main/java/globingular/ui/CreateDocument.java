package globingular.ui;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;

public class CreateDocument {

    /**
     * Create a new XML/SVG-Document with the world-map of countries.
     *
     * @return The created document
     */
    public Document createDocument() {
        try (InputStream stream = getClass().getResourceAsStream("/svg/world-states.svg")) {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory file = new SAXSVGDocumentFactory(parser);
            String uri = "/svg/world-states.svg";
            Document doc = file.createDocument(uri, stream);
            return doc;

        } catch (IOException io) {
            io.printStackTrace();
        }
        return null;
    }
}
