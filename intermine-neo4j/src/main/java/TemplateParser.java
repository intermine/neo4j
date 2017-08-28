import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.neo4j.cypher.QueryGenerator;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * This class extracts all the template path queries from the file "templates.xml" and then coverts
 * them to Cypher.
 *
 * Path Queries are stored in path specified by TARGET_DIR_PATHQUERY and converted Cypher is stored
 * in path specified by TARGET_DIR_CYPHER directory.
 *
 * The file can be downloaded from the InterMine API by using the command
 * "wget www.flymine.org/query/service/templates".
 *
 * @author Yash Sharma
 */
public class TemplateParser {

    private static String TARGET_DIR_PATHQUERY = "src/main/resources/pathquery/templates/";

    private static String TARGET_DIR_CYPHER = "src/main/resources/cypher/templates/";

    public static void main(String args[]) throws JDOMException, IOException {
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

        for (int temp = 0; temp < templates.size(); temp++) {

            Element pathQueryElement = templates.get(temp).getChild("query");
            String pathQueryName = pathQueryElement.getAttributeValue("name");
            XMLOutputter outp = new XMLOutputter();
            String pathQueryXml = outp.outputString(pathQueryElement);
            String pathQueryPathName = TARGET_DIR_PATHQUERY + pathQueryName + ".xml";
            Util.writeFile(pathQueryPathName, pathQueryXml);
            Neo4jLoaderProperties properties = new Neo4jLoaderProperties();
            QueryService service = properties.getQueryService();
            PathQuery pathQuery = service.createPathQuery(pathQueryXml);
            String cypherQueryName = TARGET_DIR_CYPHER + pathQueryName + ".cypher";

            try {
                String cypherQuery = QueryGenerator.pathQueryToCypher(pathQuery).toString();
                Util.writeFile(cypherQueryName, cypherQuery);
                System.out.println("Converted template PathQuery: " + pathQueryName);
            } catch (Exception e) {
                System.out.println("Could not convert template PathQuery : " + pathQueryName);
                e.printStackTrace();
            }
        }

        System.out.println("Done.");
    }
}
