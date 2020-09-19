package globingular.ui;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CreateDocument {

    public Document createDocument() {
        try {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory file = new SAXSVGDocumentFactory(parser);
            String uri = "/svg/world-states.svg";
            InputStream stream = getClass().getResourceAsStream("/svg/world-states.svg");
            Document doc = file.createDocument(uri, stream);
            return doc;

        } catch (IOException io) {
            io.printStackTrace();

        }

        return null;
    }

}

