package org.intermine.neo4j.metadata;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.driver.v1.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Tests Schema Generator and Model methods.
 *
 * @author Yash Sharma
 */
public class ModelTest {
    public static final String TEST_PROPERTIES_FILE = "neo4jtest.properties";

    public static Model model;

    public static Driver getNeo4jTestDriver() throws IOException {
        Properties props = new Properties();
        props.load(ModelTest.class.getClassLoader().getResourceAsStream(TEST_PROPERTIES_FILE));

        String neo4jTestUrl = props.getProperty("neo4j.test.url");
        String neo4jTestUser = props.getProperty("neo4j.test.user");
        String neo4jTestPassword = props.getProperty("neo4j.test.password");

        return GraphDatabase.driver(neo4jTestUrl, AuthTokens.basic(neo4jTestUser, neo4jTestPassword));
    }

    @BeforeClass
    public static void prepareDatabaseForTesting() throws IOException {
        Driver driver = getNeo4jTestDriver();

        // Delete all existing data from the database
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                tx.run("MATCH (n) DETACH DELETE n");
                tx.success();
                tx.close();
            }
        }

        InputStream is = ModelTest.class.getClassLoader().getResourceAsStream("loadDummyData.cypher");
        String loadDummyDataCypher = IOUtils.toString(is);

        // Load dummy data for testing
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                tx.run(loadDummyDataCypher);
                tx.success();
                tx.close();
            }
        }

        model = Neo4jSchemaGenerator.getModel(driver);
    }

    @Test
    public void verifyHasNodeLabel() throws Exception {
        Boolean actual = model.hasNodeLabel("SequenceFeature");
        Boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void verifyHasRelationshipType() throws Exception {
        Boolean actual = model.hasRelationshipType("MENTIONED_IN");
        Boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void verifyLabelHasProperty() throws Exception {
        Boolean actual = model.labelHasProperty("GOEvidence", "code");
        Boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void verifyRelationshipHasProperty() throws Exception {
        Boolean actual = model.relationshipHasProperty("EVIDENCED_BY", "dataset");
        Boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void verifyGetRelationships() throws Exception {
        Set<String> actual = model.getRelationships("Gene");
        Set<String> expected = new HashSet<>();
        expected.add("ANNOTATED_BY");
        expected.add("PARTICIPATES_IN");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void verifyGetIncomingRelationships() throws Exception {
        Set<String> actual = model.getIncomingRelationships("GOAnnotation");
        Set<String> expected = new HashSet<>();
        expected.add("ANNOTATED_BY");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void verifyGetOutgoingRelationships() throws Exception {
        Set<String> actual = model.getOutgoingRelationships("GOAnnotation");
        Set<String> expected = new HashSet<>();
        expected.add("EVIDENCED_BY");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void verifyGetLabelProperties() throws Exception {
        Set<String> actual = model.getLabelProperties("Gene");
        Set<String> expected = new HashSet<>();
        expected.add("primaryIdentifier");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void verifyGetRelationshipProperties() throws Exception {
        Set<String> actual = model.getRelationshipProperties("EVIDENCED_BY");
        Set<String> expected = new HashSet<>();
        expected.add("dataset");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void verifyGetStartLabelsOfRelationship() throws Exception {
        Set<Set<String>> actual = model.getStartLabelsOfRelationship("EVIDENCED_BY");
        Set<Set<String>> expected = new HashSet<>();
        Set<String> set = new HashSet<>();
        set.add("GOAnnotation");
        expected.add(set);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void verifyGetEndLabelsOfRelationship() throws Exception {
        Set<Set<String>> actual = model.getEndLabelsOfRelationship("EVIDENCED_BY");
        Set<Set<String>> expected = new HashSet<>();
        Set<String> set = new HashSet<>();
        set.add("GOEvidence");
        expected.add(set);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void verifyHasNodeRelationship() throws Exception {
        Boolean actual = model.hasNodeRelationship("Gene", "ANNOTATED_BY");
        Boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void verifyHasIncomingNodeRelationship() throws Exception {
        Boolean actual = model.hasIncomingNodeRelationship("GOEvidence", "EVIDENCED_BY");
        Boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void verifyHasOutgoingNodeRelationship() throws Exception {
        Boolean actual = model.hasOutgoingNodeRelationship("GOAnnotation", "EVIDENCED_BY");
        Boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void verifyLabelHasNeighbour() throws Exception {
        Boolean actual = model.labelHasNeighbour("Gene", "Pathway");
        Boolean expected = true;
        Assert.assertEquals(expected, actual);
    }
}
