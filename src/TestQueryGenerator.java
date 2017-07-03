import org.intermine.neo4j.cypher.QueryGenerator;

import java.io.*;

/**
 * Tests Query Generator.
 *
 * @author Yash Sharma
 */
public class TestQueryGenerator {

    public static void main(String args[]) throws IOException {

        String FILENAME = "constraints/WITHIN";

        String PATHQUERY_FILENAME = "tests/pathquery/" + FILENAME + ".xml";
        String pathQuery = readFile(PATHQUERY_FILENAME);

        String CYPHER_FILENAME = "tests/cypher/" + FILENAME+ ".cypher";
        String cypher = QueryGenerator.pathQueryToCypher(pathQuery);

        writeFile(CYPHER_FILENAME, cypher);

        printPathQueryCypher(pathQuery, cypher);
    }

    private static void printPathQueryCypher(String pathQuery, String cypher) {
        System.out.println("Path Query :\n" + pathQuery);
        System.out.println("-----------------------------------------");
        System.out.println("\nCypher :\n" + cypher);
        System.out.println("-----------------------------------------");
    }

    private static String readFile(String fileName) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            br.close();
            return sb.toString();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Cannot read the file.");
            System.exit(0);
        }
        return "";
    }

    private static void writeFile(String fileName, String content){
        PrintWriter out = null;
        try {
            out = new PrintWriter(fileName);
            out.println(content);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Unable to write to file.");
            System.exit(0);
        }
    }
}
