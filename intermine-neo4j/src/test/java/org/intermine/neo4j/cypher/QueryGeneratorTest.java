package org.intermine.neo4j.cypher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.pathquery.PathException;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class QueryGeneratorTest {
    private static final Logger LOG = Logger.getLogger(QueryGeneratorTest.class);

    private static final File templatesPathQueryDirectory = new File("src/test/resources/pathquery/templates");

    private static QueryService queryService;

    @BeforeClass
    public static void getQueryService() throws IOException {
        Neo4jLoaderProperties properties = new Neo4jLoaderProperties();
        queryService = properties.getQueryService();
    }

    private PathQuery getPathQuery(String name) throws IOException {
        String path = "pathquery/" + name;
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) {
            LOG.error("Could not find the required XML file: " + path);
        }
        String pathQueryString = IOUtils.toString(is).replaceAll("\n$", "");
        PathQuery pathQuery = queryService.createPathQuery(pathQueryString);
        return pathQuery;
    }

    private String getCypherQuery(String name)  throws IOException {
        String path = "cypher/" + name;
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) {
            LOG.error("Could not find the required Cypher file: " + path);
        }
        return IOUtils.toString(is).replaceAll("\n$", "");
    }

    // TO DO : Add test for EXISTS and DOES_NOT_EXIST constraints.

    @Test
    public void verifyContains() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/CONTAINS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/CONTAINS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyDoesNotContain() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/DOES_NOT_CONTAIN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/DOES_NOT_CONTAIN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyEquals() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/EQUALS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/EQUALS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyNotEquals() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/NOT_EQUALS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/NOT_EQUALS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyIn() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/IN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/IN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyNotIn() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/NOT_IN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/NOT_IN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyIsA() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/ISA.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/ISA.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyIsnt() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/ISNT.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/ISNT.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyLookUp() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/LOOKUP.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/LOOKUP.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyLoop() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/LOOP.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/LOOP.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyMatches() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/MATCHES.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/MATCHES.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyDoesNotMatch() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/DOES_NOT_MATCH.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/DOES_NOT_MATCH.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyNoneOf() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/NONE_OF.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/NONE_OF.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyOneOf() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/ONE_OF.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/ONE_OF.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyOverlaps() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/OVERLAPS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/OVERLAPS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyDoesNotOverlap() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/DOES_NOT_OVERLAP.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/DOES_NOT_OVERLAP.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyWithin() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/WITHIN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/WITHIN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyOutside() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/OUTSIDE.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/OUTSIDE.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyLessThanEquals() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/LESS_THAN_EQUALS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/LESS_THAN_EQUALS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyLessThan() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/LESS_THAN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/LESS_THAN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyGreaterThanEquals() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/GREATER_THAN_EQUALS.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/GREATER_THAN_EQUALS.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyGreaterThan() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/GREATER_THAN.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/GREATER_THAN.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyIsNull() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/IS_NULL.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/IS_NULL.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyIsNotNull() throws Exception {
        PathQuery containsConstraint = getPathQuery("constraints/IS_NOT_NULL.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("constraints/IS_NOT_NULL.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifySortOrder() throws Exception {
        PathQuery containsConstraint = getPathQuery("SortOrder.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("SortOrder.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyOuterJoin() throws Exception {
        PathQuery containsConstraint = getPathQuery("OuterJoin.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("OuterJoin.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyClassesThatAreLoadedAsRelationships() throws Exception {
        PathQuery containsConstraint = getPathQuery("Gene-chromosomeLocation.xml");
        String actualCypher = QueryGenerator.pathQueryToCypher(containsConstraint).toString();
        String expectedCypher = getCypherQuery("Gene-chromosomeLocation.cypher");
        Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
    }

    @Test
    public void verifyAllTemplatesAreConverted() throws Exception {
        for (final File fileEntry : templatesPathQueryDirectory.listFiles()) {
            if (!fileEntry.isDirectory()) {
                Scanner sc = new Scanner(fileEntry);
                String pathQueryString = "";
                while(sc.hasNextLine()){
                    String str = sc.nextLine();
                    pathQueryString += str;
                }

                PathQuery pathQuery = queryService.createPathQuery(pathQueryString);
                String actualCypher = QueryGenerator.pathQueryToCypher(pathQuery).toString();
                String cypherQueryFileName = fileEntry.getName().replaceAll("\\.xml", ".cypher");
                String expectedCypher = getCypherQuery("templates/" + cypherQueryFileName);
                System.out.println("Testing QueryGenerator with template " + fileEntry.getName());
                Assert.assertEquals(expectedCypher, actualCypher.replaceAll("\n$", ""));
            }
        }
    }
}
