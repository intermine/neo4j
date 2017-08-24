package org.intermine.neo4j.cypher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Cypher Query. This class contains all the clauses and variables
 * of the cypher query.
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

    /**
     * Gets the ORDER BY clause of the Cypher query
     *
     * @return the ORDER BY clause
     */
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

    /**
     * Get all the Order elements of the Cypher Query
     *
     * @return a list of Order objects
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * Sets the orders of the Cypher Query
     *
     * @param orders List of Order objects
     */
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    /**
     * Add an order to the Cypher query
     * @param order the order object
     */
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

    /**
     * Set an upper limit on the number of rows that you want to extract
     * from the Neo4j database by running this Cypher Query
     *
     * @param resultRowsLimit The upper limit as integer
     */
    public void setResultRowsLimit(int resultRowsLimit) {
        if (resultRowsLimit <= 0) {
            throw new IllegalArgumentException("CypherQuery : LIMIT can only be a positive integer.");
        }
        this.resultRowsLimit = resultRowsLimit;
    }

    /**
     * Remove any limit assigned to this Cypher Query
     */
    public void removeResultRowsLimit() {
        this.resultRowsLimit = -1;
    }

    /**
     * Set the number of rows that you want to skip while extracting result rows
     * from the Neo4j database by running this Cypher Query
     *
     * @param resultRowsSkip
     */
    public void setResultRowsSkip(int resultRowsSkip) {
        if (resultRowsSkip <= 0) {
            throw new IllegalArgumentException("CypherQuery : SKIP can only be a positive integer.");
        }
        this.resultRowsSkip = resultRowsSkip;
    }

    /**
     * Remove the number of rows to be skipped which is assigned to this Cypher Query.
     * After this operation cypher will not skip any rows while returning results from Neo4j.
     */
    public void removeResultRowsSkip() {
        this.resultRowsSkip = -1;
    }

    /**
     * Get the limit set on number of result rows.
     * @return the limit as integer
     */
    public int getResultRowsLimit() {
        return resultRowsLimit;
    }

    /**
     * Get the number of rows set to be skipped.
     * @return the number of skipped rows as integer
     */
    public int getResultRowsSkip() {
        return resultRowsSkip;
    }

    /**
     * Add a variable name and corresponding view to the Cypher Query.
     * @param view the view as string. For example, Gene.chromosome.primaryIdentifier.
     * @param variableName the variable name for the view.
     */
    public void addVariable(String view, String variableName) {
        variables.put(view, variableName);
    }

    /**
     * Get corresponding variable name by supplying view as parameter
     * @param view the view
     * @return the variable name for cypher query
     */
    public String getVariable(String view) {
        return variables.get(view);
    }

}
