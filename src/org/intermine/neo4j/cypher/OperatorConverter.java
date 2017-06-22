package org.intermine.neo4j.cypher;

import org.intermine.metadata.ConstraintOp;

/**
 * Handles Conversion of InterMine constraints to Neo4j constraints.
 *
 * @author Yash Sharma
 */
public class OperatorConverter {

    public static String getCypherOperator(ConstraintOp pathQueryOperator){
        // TO DO : Cover cases for all possible operators.
        return pathQueryOperator.toString();
    }

    public static String getCypherValue(ConstraintOp pathQueryOperator, String pathQueryValue){
        // TO DO : Cover cases for all possible operators.
        String value;
        switch (pathQueryOperator.toString()){
            case ("CONTAINS"):
                value = "'" + pathQueryValue + "'";
                break;
            default:
                value = pathQueryValue;
        }
        return value;
    }
}
