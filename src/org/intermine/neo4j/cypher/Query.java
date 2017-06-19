package org.intermine.neo4j.cypher;

/**
 * Represents a Cypher Query.
 *
 * @author Yash Sharma
 */
public class Query {
    private String queryString;

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public Query(String queryString) {
        this.queryString = queryString;
    }
}
