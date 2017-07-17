package org.intermine.neo4j.cypher;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class QueryGeneratorTest {
    private static final Logger LOG = Logger.getLogger(QueryGeneratorTest.class);

    // TO DO : Add test for EXISTS and DOES_NOT_EXIST constraints.

    private String getPathQuery(String name) throws IOException {
        String path = "pathquery/" + name;
        System.out.println("Path is " + path);
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) {
            LOG.error("Could not find the required XML file: " + path);
        }
        return IOUtils.toString(is).replaceAll("\n$", "");
    }

    private String getCypherQuery(String name)  throws IOException {
        String path = "cypher/" + name;
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) {
            LOG.error("Could not find the required Cypher file: " + path);
        }
        return IOUtils.toString(is).replaceAll("\n$", "");
    }

    @Test
    public void verifyContains() throws Exception {   
        String containsConstraint = getPathQuery("constraints/CONTAINS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/CONTAINS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyDoesNotContain() throws Exception {
        String containsConstraint = getPathQuery("constraints/DOES_NOT_CONTAIN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/DOES_NOT_CONTAIN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyEquals() throws Exception {
        String containsConstraint = getPathQuery("constraints/EQUALS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/EQUALS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyNotEquals() throws Exception {
        String containsConstraint = getPathQuery("constraints/NOT_EQUALS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/NOT_EQUALS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyIn() throws Exception {
        String containsConstraint = getPathQuery("constraints/IN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/IN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyNotIn() throws Exception {
        String containsConstraint = getPathQuery("constraints/NOT_IN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/NOT_IN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyIsA() throws Exception {
        String containsConstraint = getPathQuery("constraints/ISA.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/ISA.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyIsnt() throws Exception {
        String containsConstraint = getPathQuery("constraints/ISNT.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/ISNT.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyLookUp() throws Exception {
        String containsConstraint = getPathQuery("constraints/LOOKUP.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/LOOKUP.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyLoop() throws Exception {
        String containsConstraint = getPathQuery("constraints/LOOP.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/LOOP.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyMatches() throws Exception {
        String containsConstraint = getPathQuery("constraints/MATCHES.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/MATCHES.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyDoesNotMatch() throws Exception {
        String containsConstraint = getPathQuery("constraints/DOES_NOT_MATCH.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/DOES_NOT_MATCH.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyNoneOf() throws Exception {
        String containsConstraint = getPathQuery("constraints/NONE_OF.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/NONE_OF.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyOneOf() throws Exception {
        String containsConstraint = getPathQuery("constraints/ONE_OF.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/ONE_OF.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyOverlaps() throws Exception {
        String containsConstraint = getPathQuery("constraints/OVERLAPS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/OVERLAPS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyDoesNotOverlap() throws Exception {
        String containsConstraint = getPathQuery("constraints/DOES_NOT_OVERLAP.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/DOES_NOT_OVERLAP.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyWithin() throws Exception {
        String containsConstraint = getPathQuery("constraints/WITHIN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/WITHIN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyOutside() throws Exception {
        String containsConstraint = getPathQuery("constraints/OUTSIDE.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/OUTSIDE.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyLessThanEquals() throws Exception {
        String containsConstraint = getPathQuery("constraints/LESS_THAN_EQUALS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/LESS_THAN_EQUALS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyLessThan() throws Exception {
        String containsConstraint = getPathQuery("constraints/LESS_THAN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/LESS_THAN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyGreaterThanEquals() throws Exception {
        String containsConstraint = getPathQuery("constraints/GREATER_THAN_EQUALS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/GREATER_THAN_EQUALS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyGreaterThan() throws Exception {
        String containsConstraint = getPathQuery("constraints/GREATER_THAN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/GREATER_THAN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyIsNull() throws Exception {
        String containsConstraint = getPathQuery("constraints/IS_NULL.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/IS_NULL.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyIsNotNull() throws Exception {
        String containsConstraint = getPathQuery("constraints/IS_NOT_NULL.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("constraints/IS_NOT_NULL.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifySortOrder() throws Exception {
        String containsConstraint = getPathQuery("SortOrder.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("SortOrder.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyOuterJoin() throws Exception {
        String containsConstraint = getPathQuery("OuterJoin.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint);
        String expectedCypher = getCypherQuery("OuterJoin.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

}
