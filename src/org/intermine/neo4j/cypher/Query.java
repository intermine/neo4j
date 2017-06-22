package org.intermine.neo4j.cypher;

/**
 * Represents a Cypher Query.
 *
 * @author Yash Sharma
 */
public class Query {
    private String matchClause = "";
    private String optionalMatchClause = "";
    private String whereClause = "";
    private String returnClause = "";
    private String orderByClause = "";

    public void addToMatch(String string){
        if(matchClause.equals("")){
            matchClause += "MATCH " + string;
        } else {
            matchClause += ",\n" + string;
        }
    }

    public void addToOptionalMatch(String string){
        if(optionalMatchClause.equals("")){
            optionalMatchClause += "OPTIONAL MATCH " + string;
        } else {
            optionalMatchClause += ",\n" + string;
        }
    }

    public void addToReturn(String string){
        if(returnClause.equals("")){
            returnClause += "RETURN " + string;
        } else {
            returnClause += ",\n" + string;
        }
    }

    public void addToOrderBy(String string){
        if(orderByClause.equals("")){
            orderByClause += "ORDER BY " + string;
        } else {
            orderByClause += ",\n" + string;
        }
    }

    protected void setWhereClause(String whereClause){
        this.whereClause = whereClause;
    }

    public String getMatchClause() {
        return matchClause;
    }

    public String getOptionalMatchClause() {
        return optionalMatchClause;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public String getReturnClause() {
        return returnClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    @Override
    public String toString() {
        return matchClause + "\n" +
                optionalMatchClause + "\n" +
                whereClause + "\n" +
                returnClause + "\n" +
                orderByClause;
    }

}
