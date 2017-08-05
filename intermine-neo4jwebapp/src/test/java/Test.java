import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.neo4j.cypher.CypherQuery;
import org.intermine.neo4j.cypher.QueryGenerator;
import org.intermine.neo4j.model.QueryResult;
import org.intermine.neo4j.service.Neo4jQueryService;
import org.intermine.pathquery.PathException;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author Yash Sharma
 */
public class Test {
    private static final Logger LOG = Logger.getLogger(Test.class);

    public static void main(String[] args) throws IOException, ModelParserException, PathException, SAXException, ParserConfigurationException {
        Neo4jLoaderProperties props = new Neo4jLoaderProperties();
        QueryService queryService = props.getQueryService();
        PathQuery pathQuery = getPathQuery(queryService, "PathQuery.xml");
        CypherQuery cypherQuery = QueryGenerator.pathQueryToCypher(pathQuery);

        cypherQuery.setResultLimit(10);
        System.out.println("result limit set");
        QueryResult queryResult = new Neo4jQueryService().getResultsFromNeo4j(props.getGraphDatabaseDriver(),
                cypherQuery, pathQuery);
        System.out.println(queryResult);
    }

    private static PathQuery getPathQuery(QueryService queryService, String name) throws IOException {
        String path = name;
        System.out.println("Path is " + path);
        InputStream is = Test.class.getClassLoader().getResourceAsStream(path);
        if (is == null) {
            LOG.error("Could not find the required XML file: " + path);
        }
        String pathQueryString = IOUtils.toString(is).replaceAll("\n$", "");
        PathQuery pathQuery = queryService.createPathQuery(pathQueryString);
        return pathQuery;
    }
}
