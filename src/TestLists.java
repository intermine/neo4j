
import org.intermine.metadata.Model;
import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.Neo4jLoaderProperties;

import java.io.IOException;

/**
 * Gets user profile lists from InterMine web service.
 *
 * @author Yash Sharma
 */
public class TestLists {

    public static void main(String[] args) throws IOException, ModelParserException {
        Neo4jLoaderProperties props = new Neo4jLoaderProperties();
        Model model = props.getModel();

    }
}
