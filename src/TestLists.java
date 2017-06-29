
import org.intermine.metadata.Model;
import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.webservice.client.core.ContentType;
import org.intermine.webservice.client.core.Request;
import org.intermine.webservice.client.core.ServiceFactory;
import org.intermine.webservice.client.lists.ItemList;
import org.intermine.webservice.client.lists.Lists;
import org.intermine.webservice.client.results.Item;
import org.intermine.webservice.client.services.ListService;
import org.intermine.webservice.client.services.QueryService;
import org.intermine.webservice.client.util.HttpConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Gets user profile lists from InterMine web service.
 *
 * @author Yash Sharma
 */
public class TestLists {

    public static void main(String[] args) throws IOException, ModelParserException {
        Neo4jLoaderProperties props = new Neo4jLoaderProperties();
        String authToken = props.getIntermineApiToken();

        ListService listService = props.getListService();
        listService.setAuthentication(authToken);
        ItemList itemList = listService.getList("yash2");
        System.out.println(itemList);

        Iterator<Item> iterator = itemList.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
