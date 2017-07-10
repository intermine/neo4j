package org.intermine.neo4j.cypher.constraint;

import org.intermine.metadata.ConstraintOp;
import org.intermine.pathquery.PathConstraint;

/**
 * Handles Conversion of PathQuery operators to Cypher operators.
 *
 * @author Yash Sharma
 */
public class ConstraintConverter {

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

    public static ConstraintType getConstraintType(PathConstraint pathConstraint){
        ConstraintOp op = pathConstraint.getOp();

        if(op == ConstraintOp.AND){
            return ConstraintType.AND;
        }
        else if(op == ConstraintOp.CONTAINS){
            return ConstraintType.CONTAINS;
        }
        else if(op == ConstraintOp.DOES_NOT_CONTAIN){
            return ConstraintType.DOES_NOT_CONTAIN;
        }
        else if(op == ConstraintOp.DOES_NOT_EXIST){
            return ConstraintType.DOES_NOT_EXIST;
        }
        else if(op == ConstraintOp.DOES_NOT_HAVE){
            return ConstraintType.DOES_NOT_HAVE;
        }
        else if(op == ConstraintOp.DOES_NOT_MATCH){
            return ConstraintType.DOES_NOT_MATCH;
        }
        else if(op == ConstraintOp.DOES_NOT_OVERLAP){
            return ConstraintType.DOES_NOT_OVERLAP;
        }
        else if(op == ConstraintOp.EQUALS){
            return ConstraintType.EQUALS;
        }
        else if(op == ConstraintOp.EXACT_MATCH){
            return ConstraintType.EXACT_MATCH;
        }
        else if(op == ConstraintOp.EXISTS){
            return ConstraintType.EXISTS;
        }
        else if(op == ConstraintOp.GREATER_THAN){
            return ConstraintType.GREATER_THAN;
        }
        else if(op == ConstraintOp.GREATER_THAN_EQUALS){
            return ConstraintType.GREATER_THAN_EQUALS;
        }
        else if(op == ConstraintOp.HAS){
            return ConstraintType.HAS;
        }
        else if(op == ConstraintOp.IN){
            return ConstraintType.IN;
        }
        else if(op == ConstraintOp.IS_EMPTY){
            return ConstraintType.IS_EMPTY;
        }
        else if(op == ConstraintOp.IS_NOT_EMPTY){
            return ConstraintType.IS_NOT_EMPTY;
        }
        else if(op == ConstraintOp.IS_NOT_NULL){
            return ConstraintType.IS_NOT_NULL;
        }
        else if(op == ConstraintOp.IS_NULL){
            return ConstraintType.IS_NULL;
        }
        else if(op == ConstraintOp.ISA){
            return ConstraintType.ISA;
        }
        else if(op == ConstraintOp.ISNT){
            return ConstraintType.ISNT;
        }
        else if(op == ConstraintOp.LESS_THAN) {
            return ConstraintType.LESS_THAN;
        }
        else if(op == ConstraintOp.LESS_THAN_EQUALS){
            return ConstraintType.LESS_THAN_EQUALS;
        }
        else if(op == ConstraintOp.LOOKUP){
            return ConstraintType.LOOKUP;
        }
        else if(op == ConstraintOp.MATCHES){
            return ConstraintType.MATCHES;
        }
        else if(op == ConstraintOp.NAND){
            return ConstraintType.NAND;
        }
        else if(op == ConstraintOp.NONE_OF){
            return ConstraintType.NONE_OF;
        }
        else if(op == ConstraintOp.NOR){
            return ConstraintType.NOR;
        }
        else if(op == ConstraintOp.NOT_EQUALS){
            return ConstraintType.NOT_EQUALS;
        }
        else if(op == ConstraintOp.NOT_IN){
            return ConstraintType.NOT_IN;
        }
        else if(op == ConstraintOp.ONE_OF){
            return ConstraintType.ONE_OF;
        }
        else if(op == ConstraintOp.OR){
            return ConstraintType.OR;
        }
        else if(op == ConstraintOp.OUTSIDE){
            return ConstraintType.OUTSIDE;
        }
        else if(op == ConstraintOp.OVERLAPS){
            return ConstraintType.OVERLAPS;
        }
        else if(op == ConstraintOp.STRICT_NOT_EQUALS){
            return ConstraintType.STRICT_NOT_EQUALS;
        }
        else if(op == ConstraintOp.WITHIN)
        {
            return ConstraintType.WITHIN;
        }
        else {
            System.out.println("In Constraint Converter class, getConstraintType method " +
                                "Unsupported constraint found Found");
            return ConstraintType.UNSUPPORTED_CONSTRAINT;
        }
    }
}
