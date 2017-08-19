package org.intermine.neo4j.cypher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Cypher Query.
 *
 * @author Yash Sharma
 */
public class CypherQuery {

    private StringBuilder matchClause = new StringBuilder();

    private StringBuilder optionalMatchClause = new StringBuilder();

    private StringBuilder whereClause = new StringBuilder();

    private StringBuilder returnClause = new StringBuilder();

    private List<Order> orders = new ArrayList<>();

    private Map<String, String> variables = new HashMap<>();

    private int resultRowsLimit = -1;

    private int resultRowsSkip = -1;

    /**
     * Add a match to the MATCH clause of the Cypher query
     *
     * @param string the given match
     */
    protected void addToMatch(String string){
        if(matchClause.length() == 0){
            matchClause.append("MATCH " + string);
        } else {
            matchClause.append(",\n" + string);
        }
    }

    /**
     * Add a match to the OPTIONAL MATCH clause of the Cypher query
     *
     * @param string the given match
     */
    protected void addToOptionalMatch(String string){
        if(optionalMatchClause.length() == 0){
            optionalMatchClause.append("OPTIONAL MATCH " + string);
        } else {
            optionalMatchClause.append(",\n" + string);
        }
    }

    /**
     * Add a view to be added to the RETURN clause of the Cypher query
     *
     * @param string the given view
     */
    protected void addToReturn(String string){
        if(returnClause.length() == 0){
            returnClause.append("RETURN " + string);
        } else {
            returnClause.append(",\n" + string);
        }
    }



    /**
     * Sets the given string as the WHERE clause of the Cypher query
     *
     * @param whereClause the given where clause
     */
    protected void setWhereClause(String whereClause){
        this.whereClause = new StringBuilder();
        this.whereClause.append(whereClause);
    }

    /**
     * Gets the MATCH clause of the Cypher query
     *
     * @return the MATCH clause
     */
    public String getMatchClause() {
        return matchClause.toString();
    }

    /**
     * Gets the OPTIONAL MATCH clause of the Cypher query
     *
     * @return the OPTIONAL MATCH clause
     */
    public String getOptionalMatchClause() {
        return optionalMatchClause.toString();
    }

    /**
     * Gets the WHERE clause of the Cypher query
     *
     * @return the WHERE clause
     */
    public String getWhereClause() {
        return whereClause.toString();
    }

    public String getOrderByClause() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!orders.isEmpty()) {
            stringBuilder.append("ORDER BY ");
            boolean first = true;
            for (Order order : orders) {
                if (first) {
                    first = false;
                } else {
                    stringBuilder.append(",\n");
                }
                stringBuilder.append(order.getVariableName() + "." +
                                    order.getPropertyKey() + " " +
                                    order.getDirection());
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Gets the RETURN clause of the Cypher query
     *
     * @return the RETURN clause
     */
    public String getReturnClause() {
        return returnClause.toString();
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    /**
     * Converts the Query object into its String representation which can be queried against
     * a Neo4j database.
     *
     * @return the Cypher query string
     */
    @Override
    public String toString() {
        StringBuilder query = new StringBuilder();

        query.append(getMatchClause() + "\n");

        if (optionalMatchClause.length() != 0) {
            query.append(getOptionalMatchClause() + "\n");
        }

        if (whereClause.length() != 0) {
            query.append(getWhereClause() + "\n");
        }

        query.append(getReturnClause() + "\n");

        if (!orders.isEmpty()) {
            query.append(getOrderByClause());
        }

        if (resultRowsSkip != -1) {
            query.append("\nSKIP " + String.valueOf(resultRowsSkip));
        }

        if (resultRowsLimit != -1) {
            query.append("\nLIMIT " + String.valueOf(resultRowsLimit));
        }

        return query.toString();
    }

    public void setResultRowsLimit(int resultRowsLimit) {
        if (resultRowsLimit <= 0) {
            throw new IllegalArgumentException("CypherQuery : LIMIT can only be a positive integer.");
        }
        this.resultRowsLimit = resultRowsLimit;
    }

    public void removeResultRowsLimit() {
        this.resultRowsLimit = -1;
    }

    public void setResultRowsSkip(int resultRowsSkip) {
        if (resultRowsSkip <= 0) {
            throw new IllegalArgumentException("CypherQuery : SKIP can only be a positive integer.");
        }
        this.resultRowsSkip = resultRowsSkip;
    }

    public void removeResultRowsSkip() {
        this.resultRowsSkip = -1;
    }

    public int getResultRowsLimit() {
        return resultRowsLimit;
    }

    public int getResultRowsSkip() {
        return resultRowsSkip;
    }

    public void addVariable(String view, String variableName) {
        variables.put(view, variableName);
    }

    public String getVariable(String view) {
        return variables.get(view);
    }

}
