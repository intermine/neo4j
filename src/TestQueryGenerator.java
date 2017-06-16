import org.intermine.neo4j.cypher.QueryGenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Tests Query Generator.
 *
 * @author Yash Sharma
 */
public class TestQueryGenerator {

    public static void main(String args[]) throws IOException {
        String pathQuery = readFile("pathqueries/sample.xml");

        printPathQueryCypher(pathQuery);
    }

    private static void printPathQueryCypher(String query) throws IOException {
        System.out.println("Path Query :\n" + query);
        System.out.println("-----------------------------------------");
        System.out.println("Cypher :\n" + QueryGenerator.pathQueryToCypher(query));
        System.out.println("-----------------------------------------");
    }

    private static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }
}
