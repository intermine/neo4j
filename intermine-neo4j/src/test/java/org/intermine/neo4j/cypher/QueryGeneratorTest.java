package org.intermine.neo4j.cypher;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class QueryGeneratorTest {
    private static final Logger LOG = Logger.getLogger(QueryGeneratorTest.class);

    private String getConstraint(String name) throws IOException {
        String path = "pathquery/constraints/" + name;
        System.out.println("path is " + path);
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) {
            LOG.error("Could not find the required XML file: " + path);
        }
        return IOUtils.toString(is).replaceAll("\n$", "");
    }

    private String getChyper(String name)  throws IOException {
        String path = "cypher/constraints/" + name;
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) {
            LOG.error("Could not find the required XML file: " + path);
        }
        return IOUtils.toString(is).replaceAll("\n$", "");
    }

    @Test
    public void verifyContains() throws Exception {   
        String containsConstraint = getConstraint("CONTAINS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getChyper("CONTAINS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyEquals() throws Exception {
        String containsConstraint = getConstraint("EQUALS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getChyper("EQUALS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }
}
