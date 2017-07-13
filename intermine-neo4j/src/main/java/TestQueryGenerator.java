import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.cypher.QueryGenerator;
import org.intermine.pathquery.PathException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


/**
 * Tests Query Generator.
 *
 * @author Yash Sharma
 */
public class TestQueryGenerator {

    public static void main(String args[]) throws IOException, PathException, ModelParserException, SAXException, ParserConfigurationException {

        String FILENAME = "outerjoin/1";

        String PATHQUERY_FILENAME = "src/main/resources/pathquery/" + FILENAME + ".xml";
        String pathQuery = Util.readFile(PATHQUERY_FILENAME);

        String CYPHER_FILENAME = "src/main/resources/cypher/" + FILENAME+ ".cypher";
        String cypher = QueryGenerator.pathQueryToCypher(pathQuery);

        Util.writeFile(CYPHER_FILENAME, cypher);

        printPathQueryCypher(pathQuery, cypher);
    }

    private static void printPathQueryCypher(String pathQuery, String cypher) {
        System.out.println("Path Query :\n" + pathQuery);
        System.out.println("-----------------------------------------");
        System.out.println("\nCypher :\n" + cypher);
        System.out.println("-----------------------------------------");
    }

}
