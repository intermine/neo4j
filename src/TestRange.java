
import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.neo4j.cypher.constraint.GenomicInterval;
import org.intermine.pathquery.PathConstraint;
import org.intermine.pathquery.PathConstraintRange;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

/**
 * Created by yash on 30/6/17.
 */
public class TestRange {

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

    public static void main(String[] args) throws IOException, ModelParserException {

        String PATHQUERY_FILENAME = "tests/pathquery/constraints/OVERLAPS.xml";
        String pathQueryString = readFile(PATHQUERY_FILENAME);
        Neo4jLoaderProperties props = new Neo4jLoaderProperties();
        QueryService service = props.getQueryService();
        PathQuery pathQuery = service.createPathQuery(pathQueryString);

        Set<PathConstraint> set = pathQuery.getConstraints().keySet();
        PathConstraint pathConstraint = set.iterator().next();


        List<String> ranges = new ArrayList<>(PathConstraint.getValues(pathConstraint));
        System.out.println(ranges);
        PathConstraintRange pcr = new PathConstraintRange(pathConstraint.getPath(),
                                                            pathConstraint.getOp(),
                                                            ranges);
        System.out.println("-------------------------------");
        for (String range: pcr.getValues()) {
            GenomicInterval interval = new GenomicInterval(range);
            System.out.println(interval.getStart());
            System.out.println(interval.getEnd());
            System.out.println(interval.getChr());
            System.out.println(interval.getTaxonId());
        }
    }


}
