import org.intermine.neo4j.cypher.QueryGenerator;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * This class extracts queries from templates which are stored in resources/templates.xml file,
 * stores each query in tests/pathquery/templates directory, converts each PathQuery to Cypher
 * and stores it in tests/cypher/templates directory.
 *
 * @author Yash Sharma
 */
public class TemplateParser {
    public static void main(String args[]) throws JDOMException {
    	InputStream inputStream = TemplateParser.class.getResourceAsStream("templates.xml");

        SAXBuilder saxBuilder = new SAXBuilder();

        Document document;
        try {
            document = saxBuilder.build(inputStream);
        } catch (IOException e) {
        	System.out.println("Could not read file template.xml");
            return;
        }

        Element classElement = document.getRootElement();
        List<Element> templates = classElement.getChildren();

        String BASE_DIR_PATHQUERY = "src/main/resources/pathquery/templates/";
        String BASE_DIR_CYPHER = "src/main/resources/cypher/templates/";

        for (int temp = 0; temp < templates.size(); temp++) {
            Element pathQuery = templates.get(temp).getChild("query");
            String pathQueryName = pathQuery.getAttributeValue("name");

            XMLOutputter outp = new XMLOutputter();
            String pathQueryXml = outp.outputString(pathQuery);

            String pathQueryPathName = BASE_DIR_PATHQUERY + pathQueryName + ".xml";
            Util.writeFile(pathQueryPathName, pathQueryXml);

            String cypherQueryName = BASE_DIR_CYPHER + pathQueryName + ".cypher";
            try {
                Util.writeFile(cypherQueryName, QueryGenerator.pathQueryToCypher(pathQueryXml));
                System.out.println("Converted template PathQuery: " + pathQueryName);
            } catch (Exception e) {
                System.out.println("Could not convert template PathQuery : " + pathQueryName);
                e.printStackTrace();
            }
        }
        System.out.println("Done.");
    }
}
