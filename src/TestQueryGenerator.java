import org.intermine.neo4j.cypher.QueryGenerator;

import java.io.IOException;


/**
 * Tests Query Generator.
 *
 * @author Yash Sharma
 */
public class TestQueryGenerator {

    public static void main(String args[]) throws IOException {

        String FILENAME = "constraints/LOOP";

        String PATHQUERY_FILENAME = "tests/pathquery/" + FILENAME + ".xml";
        String pathQuery = Util.readFile(PATHQUERY_FILENAME);

        String CYPHER_FILENAME = "tests/cypher/" + FILENAME+ ".cypher";
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
