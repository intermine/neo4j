package org.intermine.neo4j.cypher;

/**
 * Represents a Cypher Query.
 *
 * @author Yash Sharma
 */
public class CypherQuery {

    private String matchClause = "";

    private String optionalMatchClause = "";

    private String whereClause = "";

    private String returnClause = "";

    private String orderByClause = "";

    /**
     * Add a match to the MATCH clause of the Cypher query
     *
     * @param string the given match
     */
    protected void addToMatch(String string){
        if(matchClause.equals("")){
            matchClause += "MATCH " + string;
        } else {
            matchClause += ",\n" + string;
        }
    }

    /**
     * Add a match to the OPTIONAL MATCH clause of the Cypher query
     *
     * @param string the given match
     */
    protected void addToOptionalMatch(String string){
        if(optionalMatchClause.equals("")){
            optionalMatchClause += "OPTIONAL MATCH " + string;
        } else {
            optionalMatchClause += ",\n" + string;
        }
    }

    /**
     * Add a view to be added to the RETURN clause of the Cypher query
     *
     * @param string the given view
     */
    protected void addToReturn(String string){
        if(returnClause.equals("")){
            returnClause += "RETURN " + string;
        } else {
            returnClause += ",\n" + string;
        }
    }

    /**
     * Add an order to the ORDER BY clause of the Cypher query
     *
     * @param string the given order
     */
    protected void addToOrderBy(String string){
        if(orderByClause.equals("")){
            orderByClause += "ORDER BY " + string;
        } else {
            orderByClause += ",\n" + string;
        }
    }

    /**
     * Sets the given string as the WHERE clause of the Cypher query
     *
     * @param whereClause the given where clause
     */
    protected void setWhereClause(String whereClause){
        if (!whereClause.startsWith("WHERE ")){
            whereClause = "WHERE " + whereClause;
        }
        this.whereClause = whereClause;
    }

    /**
     * Gets the MATCH clause of the Cypher query
     *
     * @return the MATCH clause
     */
    public String getMatchClause() {
        return matchClause;
    }

    /**
     * Gets the OPTIONAL MATCH clause of the Cypher query
     *
     * @return the OPTIONAL MATCH clause
     */
    public String getOptionalMatchClause() {
        return optionalMatchClause;
    }

    /**
     * Gets the WHERE clause of the Cypher query
     *
     * @return the WHERE clause
     */
    public String getWhereClause() {
        return whereClause;
    }

    /**
     * Gets the RETURN clause of the Cypher query
     *
     * @return the RETURN clause
     */
    public String getReturnClause() {
        return returnClause;
    }

    /**
     * Gets the ORDER BY clause of the Cypher query
     *
     * @return the ORDER BY clause
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * Converts the Query object into its String representation which can be queried against
     * a Neo4j database.
     *
     * @return the Cypher query string
     */
    @Override
    public String toString() {
        return matchClause + "\n" +
                optionalMatchClause + "\n" +
                whereClause + "\n" +
                returnClause + "\n" +
                orderByClause;
    }

}
