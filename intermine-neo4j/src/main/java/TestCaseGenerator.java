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
 * Converts all PathQueries inside test/resources/pathquery to their Cypher Equivalents and
 * store cypher in test/resources/cypher.
 *
 * @author Yash Sharma
 */
public class TestCaseGenerator {
    public static void main(String[] args) throws IOException, PathException, ModelParserException, ParserConfigurationException, SAXException {
        final File fromFolder = new File("src/test/resources/pathquery");
        final File toFolder = new File("src/test/resources/cypher");
        generateCypherForAllFiles(fromFolder, toFolder);
    }

    public static void generateCypherForAllFiles(final File fromfolder, final File toFolder) throws IOException, PathException, ModelParserException, ParserConfigurationException, SAXException {
        for (final File fileEntry : fromfolder.listFiles()) {
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
                String cypherName = toFolder.getPath() + "/" +
                        fileEntry.getName().replaceAll("\\.xml", ".cypher");
                Util.writeFile(cypherName, cypherQuery.toString());
            }
        }
    }


}
