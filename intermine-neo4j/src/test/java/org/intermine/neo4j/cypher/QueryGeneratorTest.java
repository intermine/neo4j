package org.intermine.neo4j.cypher;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
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

    private String getCypher(String name)  throws IOException {
        String path = "cypher/constraints/" + name;
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) {
            LOG.error("Could not find the required Cypher file: " + path);
        }
        return IOUtils.toString(is).replaceAll("\n$", "");
    }

    @Test
    public void verifyContains() throws Exception {   
        String containsConstraint = getConstraint("CONTAINS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("CONTAINS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyDoesNotContain() throws Exception {
        String containsConstraint = getConstraint("DOES_NOT_CONTAIN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("DOES_NOT_CONTAIN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyEquals() throws Exception {
        String containsConstraint = getConstraint("EQUALS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("EQUALS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyNotEquals() throws Exception {
        String containsConstraint = getConstraint("NOT_EQUALS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("NOT_EQUALS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyIn() throws Exception {
        String containsConstraint = getConstraint("IN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("IN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyNotIn() throws Exception {
        String containsConstraint = getConstraint("NOT_IN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("NOT_IN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyIsA() throws Exception {
        String containsConstraint = getConstraint("ISA.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("ISA.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyIsnt() throws Exception {
        String containsConstraint = getConstraint("ISNT.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("ISNT.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyLookUp() throws Exception {
        String containsConstraint = getConstraint("LOOKUP.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("LOOKUP.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyLoop() throws Exception {
        String containsConstraint = getConstraint("LOOP.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("LOOP.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyMatches() throws Exception {
        String containsConstraint = getConstraint("MATCHES.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("MATCHES.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyDoesNotMatch() throws Exception {
        String containsConstraint = getConstraint("DOES_NOT_MATCH.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("DOES_NOT_MATCH.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyNoneOf() throws Exception {
        String containsConstraint = getConstraint("NONE_OF.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("NONE_OF.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyOneOf() throws Exception {
        String containsConstraint = getConstraint("ONE_OF.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("ONE_OF.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyOverlaps() throws Exception {
        String containsConstraint = getConstraint("OVERLAPS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("OVERLAPS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyDoesNotOverlap() throws Exception {
        String containsConstraint = getConstraint("DOES_NOT_OVERLAP.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("DOES_NOT_OVERLAP.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyWithin() throws Exception {
        String containsConstraint = getConstraint("WITHIN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("WITHIN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyOutside() throws Exception {
        String containsConstraint = getConstraint("OUTSIDE.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("OUTSIDE.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyLessThanEquals() throws Exception {
        String containsConstraint = getConstraint("LESS_THAN_EQUALS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("LESS_THAN_EQUALS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyLessThan() throws Exception {
        String containsConstraint = getConstraint("LESS_THAN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("LESS_THAN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyGreaterThanEquals() throws Exception {
        String containsConstraint = getConstraint("GREATER_THAN_EQUALS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("GREATER_THAN_EQUALS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyGreaterThan() throws Exception {
        String containsConstraint = getConstraint("GREATER_THAN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("GREATER_THAN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyIsNull() throws Exception {
        String containsConstraint = getConstraint("IS_NULL.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("IS_NULL.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyIsNotNull() throws Exception {
        String containsConstraint = getConstraint("IS_NOT_NULL.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("IS_NOT_NULL.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyExists() throws Exception {
        String containsConstraint = getConstraint("EXISTS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("EXISTS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyDoesNotExist() throws Exception {
        String containsConstraint = getConstraint("DOES_NOT_EXIST.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("DOES_NOT_EXIST.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyOr() throws Exception {
        String containsConstraint = getConstraint("OR.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("OR.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyNor() throws Exception {
        String containsConstraint = getConstraint("NOR.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("NOR.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyAnd() throws Exception {
        String containsConstraint = getConstraint("AND.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("AND.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyNand() throws Exception {
        String containsConstraint = getConstraint("NAND.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypher("NAND.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }
}
