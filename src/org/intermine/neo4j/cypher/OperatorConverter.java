package org.intermine.neo4j.cypher;

import org.intermine.metadata.ConstraintOp;

/**
 * Handles Conversion of PathQuery operators to Cypher operators.
 *
 * @author Yash Sharma
 */
public class OperatorConverter {

    /**
     * Gets the cypher operator corresponding to the given PathQuery Constraint Operator
     *
     * @param pathQueryOperator the given PathQuery Constraint Operator
     * @return the cypher operator
     */
    public static String getCypherOperator(ConstraintOp pathQueryOperator){
        // TO DO : Cover cases for all possible operators.
        return pathQueryOperator.toString();
    }

    /**
     * Transforms the given PathQuery constraint value into Cypher usable form and as per the given
     * PathQuery constraint operator
     *
     * @param pathQueryOperator the given PathQuery constraint operator
     * @param pathQueryValue the given PathQuery constraint value
     * @return the constraint value in cypher usable form
     */
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
