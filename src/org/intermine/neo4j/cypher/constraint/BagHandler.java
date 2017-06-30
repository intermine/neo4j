package org.intermine.neo4j.cypher.constraint;

import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.webservice.client.lists.ItemList;
import org.intermine.webservice.client.results.Item;
import org.intermine.webservice.client.services.ListService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Stores various methods that deal with InterMine Lists (Bags).
 *
 * @author Yash Sharma
 */
public class BagHandler {

    private static ListService getListService(){
        Neo4jLoaderProperties props = null;
        try {
            props = new Neo4jLoaderProperties();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load neo4jloader.properties file.");
        }
        String authToken = props.getIntermineApiToken();
        return props.getListService(authToken);
    }

    public static Set<String> getListNames(){
        return getListService().getListMap().keySet();
    }

    public static boolean listExists(String listName){
        return getListNames().contains(listName);
    }

    public static Set<String> getAttributes(String listName, String entityName, String attributeName){
        Set<String> set = new HashSet<>();
        if (!listExists(listName)) {
            return set;
        }
        ListService listService = getListService();
        ItemList items = listService.getList(listName);
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            set.add(iterator.next().getString(entityName + "." + attributeName));
        }
        return set;
    }
}
