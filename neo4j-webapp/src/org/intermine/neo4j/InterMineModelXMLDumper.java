package org.intermine.neo4j;

import org.intermine.metadata.Model;
import org.intermine.webservice.client.core.ServiceFactory;

/**
 * Simple utility dump an InterMine model in XML form to standard output.
 *
 * @author Sam Hokin
 */
public class InterMineModelXMLDumper {

    /**
     * @param args command line arguments
     * @throws IOException
     */
    public static void main(String[] args) {

        // args
        if (args.length!=1) {
            System.out.println("Usage: InterMineModelXMLDumper <InterMine service URL>");
            System.out.println("Example: InterMineModelXMLDumper http://www.synbiomine.org/synbiomine/service");
            System.exit(0);
        }
        String intermineServiceUrl = args[0];

        // InterMine setup
        ServiceFactory factory = new ServiceFactory(intermineServiceUrl);
        Model model = factory.getModel();
        System.out.println(model.toString());

    }

}
