package org.intermine.neo4j.cypher;


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

            case NAND:
                constraint = "NOT (" +
                            join(treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName(),
                            "AND",
                            PathConstraint.getValue(pathConstraint)) + ")";
                break;

            case OR:
                constraint = join(treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName(),
                            "OR",
                            PathConstraint.getValue(pathConstraint));
                break;

            case NOR:
                constraint = "NOT (" +
                            join(treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName(),
                            "OR",
                            PathConstraint.getValue(pathConstraint)) + ")";
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

            // IS NOT EMPTY is synonymous to IS NOT NULL
            case IS_NOT_EMPTY:
            case IS_NOT_NULL:
                constraint = treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName() + " " +
                            "IS NOT NULL";
                break;

            // IS EMPTY is synonymous to IS NULL
            case IS_EMPTY:
            case IS_NULL:
                constraint = treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName() + " " +
                            "IS NULL";
                break;

            case ONE_OF:
                constraint = treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName() + " " +
                            "IN ";
                constraint += Helper.quoted(PathConstraint.getValues(pathConstraint));
                break;

            case NONE_OF:
                constraint = "NOT " +
                            treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName() + " " +
                            "IN ";
                constraint += Helper.quoted(PathConstraint.getValues(pathConstraint));
                break;

            case EXISTS:
                constraint = "exists(" +
                            treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName() + ")";
                break;

            case DOES_NOT_EXIST:
                constraint = "NOT exists(" +
                            treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName() + ")";
                break;

            case LOOKUP:
                if(PathConstraint.getExtraValue(pathConstraint).equals(null)){
                    constraint = "ANY (key in keys(" + treeNode.getVariableName() +
                                ") WHERE " + treeNode.getVariableName() + "[key]=" +
                                Helper.quoted(PathConstraint.getValue(pathConstraint)) +
                                ")";
                }
                else{
                    constraint = "ANY (key in keys(" + treeNode.getVariableName() +
                                ") WHERE " + treeNode.getVariableName() + "[key]=" +
                                Helper.quoted(PathConstraint.getValue(pathConstraint)) +
                                ")";

                    if (ExtraValueBag.isExtraConstraint(treeNode.getGraphicalName())){
                        ExtraValueBag extraValueBag = ExtraValueBag.getExtraValueBag();
                        constraint = "( " + constraint + " AND ";
                        constraint += "(" + treeNode.getVariableName() + ")-[]-(" +
                                        extraValueBag.getLabel() + " { " +
                                        extraValueBag.getProperty() + ": " +
                                        Helper.quoted(PathConstraint.getExtraValue(pathConstraint)) +
                                        " } ))";
                    }
                }
                break;

            // Matches properties with Regular Expression
            case MATCHES:
                constraint = treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName() + " =~ " +
                            Helper.quoted(PathConstraint.getValue(pathConstraint));
                break;

            case EXACT_MATCH:
                constraint = treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName() + " = " +
                            Helper.quoted(PathConstraint.getValue(pathConstraint));
                break;

            case DOES_NOT_MATCH:
                constraint = "NOT " +
                            treeNode.getParent().getVariableName() + "." +
                            treeNode.getGraphicalName() + " =~ " +
                            Helper.quoted(PathConstraint.getValue(pathConstraint));
                break;

            case IN:
                constraint = treeNode.getGraphicalName() + ".primaryIdentifier IN " +
                Helper.quoted(BagHandler.getAttributes(PathConstraint.getValue(pathConstraint),
                                                        treeNode.getGraphicalName(),
                                                        "primaryIdentifier"));
                break;

            case NOT_IN:
                constraint = "NOT " + treeNode.getGraphicalName() + ".primaryIdentifier IN " +
                Helper.quoted(BagHandler.getAttributes(PathConstraint.getValue(pathConstraint),
                                                        treeNode.getGraphicalName(),
                                                        "primaryIdentifier"));
                break;

            case STRICT_NOT_EQUALS:

            case HAS:

            case DOES_NOT_HAVE:

            case ISA:

            case ISNT:

            case WITHIN:

            case OUTSIDE:

            case OVERLAPS:

            case DOES_NOT_OVERLAP:

            case UNSUPPORTED_CONSTRAINT:
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
