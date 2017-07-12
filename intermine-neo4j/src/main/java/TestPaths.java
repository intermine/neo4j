import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.neo4j.Neo4jModelParser;
import org.intermine.neo4j.cypher.OntologyConverter;
import org.intermine.pathquery.Path;
import org.intermine.pathquery.PathException;

import java.io.IOException;

public class TestPaths {
    public static void main(String args[]) throws PathException, IOException, ModelParserException {
        Neo4jModelParser modelParser = new Neo4jModelParser();
        Neo4jLoaderProperties props = new Neo4jLoaderProperties();

        String pathString = "Gene.chromosome.sequence";
        Path path = new Path(props.getModel(), pathString);
        
        System.out.println(OntologyConverter.getTreeNodeType(path));

    }
}
