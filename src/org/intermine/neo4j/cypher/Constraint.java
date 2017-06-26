package org.intermine.neo4j.cypher;

import apoc.help.Help;
import org.apache.commons.lang.math.NumberUtils;
import org.intermine.metadata.ConstraintOp;
import org.intermine.pathquery.PathConstraint;

/**
 * Describes a constraint in Cypher Query.
 *
 * @author Yash Sharma
 */
public class Constraint {

    private ConstraintType type;

    private String constraint;

    private String join(String operand1, String operator, String operand2){
        return operand1 + " " + operator + " " + operand2;
    }

    Constraint(PathConstraint pathConstraint, PathTree pathTree){
        this.type = ConstraintConverter.getConstraintType(pathConstraint);
        TreeNode treeNode = pathTree.getTreeNode(pathConstraint.getPath());
        String value;
        switch (type){
            case AND:
                constraint = join(treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName(),
                            "AND",
                            PathConstraint.getValue(pathConstraint));
                break;

            case CONTAINS:
                constraint = join(treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName(),
                            "CONTAINS",
                            Helper.quoted(PathConstraint.getValue(pathConstraint)));
                break;

            case DOES_NOT_CONTAIN:
                constraint = "NOT " +
                            join(treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName(),
                            "CONTAINS",
                            Helper.quoted(PathConstraint.getValue(pathConstraint)));
                break;

//            case DOES_NOT_EXIST:
//                break;

//            case DOES_NOT_HAVE:
//                break;

//            case DOES_NOT_MATCH:
//                break;

//            case DOES_NOT_OVERLAP:
//                break;

            case EQUALS:
                value = PathConstraint.getValue(pathConstraint);
                if(!Helper.isNumeric(value)){
                    value = Helper.quoted(value);
                }
                constraint = join(treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName(),
                            "=",
                            value);
                break;

//            case EXACT_MATCH:
//                break;

//            case EXISTS:
//                break;

            case GREATER_THAN:
                constraint = join(treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName(),
                            "=",
                            PathConstraint.getValue(pathConstraint));
                break;

            case GREATER_THAN_EQUALS:
                constraint = join(treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName(),
                            ">=",
                            PathConstraint.getValue(pathConstraint));
                break;

//            case HAS:
//                break;

//            case IN:
//                break;

//            case IS_EMPTY:
//                break;

//            case IS_NOT_EMPTY:
//                break;

            case IS_NOT_NULL:
                constraint = treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName() + " " +
                            "IS NOT NULL";
                break;

            case IS_NULL:
                constraint = treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName() + " " +
                            "IS NULL";
                break;

//            case ISA:
//                break;

//            case ISNT:
//                break;

            case LESS_THAN:
                constraint = join(treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName(),
                            "<",
                            PathConstraint.getValue(pathConstraint));
                break;

            case LESS_THAN_EQUALS:
                constraint = join(treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName(),
                            "<=",
                            PathConstraint.getValue(pathConstraint));
                break;

            case LOOKUP:
                if(PathConstraint.getExtraValue(pathConstraint).equals(null)){
                    constraint = "ANY (key in keys(" + treeNode.getVariableName() +
                                ") WHERE " + treeNode.getVariableName() + "[key]=" +
                                Helper.quoted(PathConstraint.getValue(pathConstraint)) +
                                ")";
                }
                else{
                    // TO DO : Have to handle extra value here
                    constraint = "ANY (key in keys(" + treeNode.getVariableName() +
                                ") WHERE " + treeNode.getVariableName() + "[key]=" +
                                Helper.quoted(PathConstraint.getValue(pathConstraint)) +
                                ")";
                }
                break;

//            case MATCHES:
//                break;

            case NAND:
                constraint = join(treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName(),
                            "NAND",
                            PathConstraint.getValue(pathConstraint));
                break;

//            case NONE_OF:
//                break;

            case NOR:
                constraint = join(treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName(),
                            "NOR",
                            PathConstraint.getValue(pathConstraint));
                break;

            case NOT_EQUALS:
                value = PathConstraint.getValue(pathConstraint);
                if(!Helper.isNumeric(value)){
                    value = Helper.quoted(value);
                }
                constraint = "NOT " +
                            join(treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName(),
                            "<>",
                            value);
                break;

//            case NOT_IN:
//                break;

//            case ONE_OF:
//                break;

            case OR:
                constraint = join(treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName(),
                            "OR",
                            PathConstraint.getValue(pathConstraint));
                break;

//            case OUTSIDE:
//                break;

//            case OVERLAPS:
//                break;

//            case STRICT_NOT_EQUALS:
//                break;

//            case WITHIN:
//                break;

//            case LIKE:
//                break;

//            case NOT_LIKE:
//                break;

            case SOMETHING_NEW:
                this.constraint = "<NEW OPERATOR CONSTRAINT>";
                break;
        }
    }

    /**
     * Converts a cypher constraint to its string representation
     *
     * @return the string representation of the constraint
     */
    @Override
    public String toString(){
        return constraint;
    }

}
