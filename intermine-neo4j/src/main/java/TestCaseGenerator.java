import org.intermine.metadata.ModelParserException;

import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.neo4j.cypher.CypherQuery;
import org.intermine.neo4j.cypher.QueryGenerator;
import org.intermine.pathquery.PathException;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Converts all PathQueries inside directory specified by SOURCE_DIR_PATHQUERY to Cypher and
 * stores the generated cypher inside directory specified by TARGET_DIR_CYPHER.
 *
 * @author Yash Sharma
 */
public class TestCaseGenerator {

    private static String SOURCE_DIR_PATHQUERY = "src/test/resources/pathquery/constraints";

    private static String TARGET_DIR_CYPHER = "src/test/resources/cypher/constraints";

    public static void main(String[] args) throws IOException, PathException, ModelParserException, ParserConfigurationException, SAXException {
        final File fromFolder = new File(SOURCE_DIR_PATHQUERY);
        final File toFolder = new File(TARGET_DIR_CYPHER);
        generateCypherForAllFiles(fromFolder, toFolder);
    }

    /**
     * Converts all PathQueries inside directory specified by fromFolder to Cypher and
     * stores the generated cypher inside directory specified by toFolder.
     * @param fromFolder Path to source directory
     * @param toFolder Path to target directory
     * @throws IOException
     * @throws PathException
     * @throws ModelParserException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static void generateCypherForAllFiles(final File fromFolder, final File toFolder) throws IOException, PathException, ModelParserException, ParserConfigurationException, SAXException {
        for (final File fileEntry : fromFolder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                Scanner sc = new Scanner(fileEntry);
                String pathQueryString = "";

                while(sc.hasNextLine()){
                    String str = sc.nextLine();
                    pathQueryString += str;
                }

                System.out.println("Converting " + fileEntry.getName());
                Neo4jLoaderProperties properties = new Neo4jLoaderProperties();
                QueryService queryService = properties.getQueryService();
                PathQuery pathQuery = queryService.createPathQuery(pathQueryString);
                CypherQuery cypherQuery = QueryGenerator.pathQueryToCypher(pathQuery);
                String cypherName = toFolder.getPath() +
                                    "/" +
                                    fileEntry.getName().replaceAll("\\.xml", ".cypher");
                Util.writeFile(cypherName, cypherQuery.toString());
            }
        }
    }


}
