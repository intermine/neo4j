package org.intermine.neo4j.cypher.constraint;


import org.intermine.neo4j.cypher.Helper;
import org.intermine.neo4j.cypher.tree.PathTree;
import org.intermine.neo4j.cypher.tree.TreeNode;
import org.intermine.pathquery.PathConstraint;
import org.intermine.pathquery.PathConstraintRange;

import java.util.ArrayList;
import java.util.List;

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

    private String negation(String constraint){
        return "NOT " + constraint;
    }

    private String getAndConstraint(TreeNode treeNode, PathConstraint pathConstraint){
        return join(treeNode.getParent().getVariableName() + "." +
                    treeNode.getGraphicalName(),
                    "AND",
                    PathConstraint.getValue(pathConstraint));
    }

    private String getOrConstraint(TreeNode treeNode, PathConstraint pathConstraint){
        return join(treeNode.getParent().getVariableName() + "." +
                    treeNode.getGraphicalName(),
                    "OR",
                    PathConstraint.getValue(pathConstraint));
    }

    private String getEqualsConstraint(TreeNode treeNode, PathConstraint pathConstraint){
        String value = PathConstraint.getValue(pathConstraint);
        if (!Helper.isNumeric(value)) {
            value = Helper.quoted(value);
        }
        return join(treeNode.getParent().getVariableName() + "." +
                    treeNode.getGraphicalName(),
                    "=",
                    value);
    }

    private String getOneOfConstraint(TreeNode treeNode, PathConstraint pathConstraint){
        return join(treeNode.getParent().getVariableName() + "." +
                treeNode.getGraphicalName(),
                "IN",
                Helper.quoted(PathConstraint.getValues(pathConstraint)).toString());
    }

    private String getExistsConstraint(TreeNode treeNode){
        return "exists(" +
                treeNode.getParent().getVariableName() + "." +
                treeNode.getGraphicalName() + ")";
    }

    private String getLookupConstraint(TreeNode treeNode, PathConstraint pathConstraint){
        if (PathConstraint.getExtraValue(pathConstraint) == null) {
            return "ANY (key in keys(" + treeNode.getVariableName() +
                    ") WHERE " + treeNode.getVariableName() + "[key]=" +
                    Helper.quoted(PathConstraint.getValue(pathConstraint)) +
                    ")";
        }
        else {
            String string = "ANY (key in keys(" + treeNode.getVariableName() +
                            ") WHERE " + treeNode.getVariableName() + "[key]=" +
                            Helper.quoted(PathConstraint.getValue(pathConstraint)) +
                            ")";

            if (ExtraValue.isExtraConstraint(treeNode.getGraphicalName())) {
                ExtraValue extraValue = ExtraValue.getExtraValueBag();
                string = "( " + string + " AND ";
                string += "(" + treeNode.getVariableName() + ")-[]-(" +
                        extraValue.getLabel() + " { " +
                        extraValue.getProperty() + ": " +
                        Helper.quoted(PathConstraint.getExtraValue(pathConstraint)) +
                        " } ))";
            }
            return string;
        }
    }

    private String getMatchesConstraint(TreeNode treeNode, PathConstraint pathConstraint){
        return treeNode.getParent().getVariableName() + "." +
                treeNode.getGraphicalName() + " =~ " +
                Helper.quoted(PathConstraint.getValue(pathConstraint));
    }

    private String getInConstraint(TreeNode treeNode, PathConstraint pathConstraint){
        return treeNode.getGraphicalName() +
                ".primaryIdentifier IN " +
                Helper.quoted(BagHandler.getAttributes(PathConstraint.getValue(pathConstraint),
                                treeNode.getGraphicalName(),
                                "primaryIdentifier"));
    }

    private String getIsNullConstraint(TreeNode treeNode){
        return treeNode.getParent().getVariableName() + "." +
                treeNode.getGraphicalName() + " " +
                "IS NULL";
    }

    private String getGreaterThanConstraint(TreeNode treeNode, PathConstraint pathConstraint){
        return join(treeNode.getParent().getVariableName() + "." +
                treeNode.getGraphicalName(),
                ">",
                PathConstraint.getValue(pathConstraint));
    }

    private String getGreaterThanEqualsConstraint(TreeNode treeNode, PathConstraint pathConstraint){
        return join(treeNode.getParent().getVariableName() + "." +
                treeNode.getGraphicalName(),
                ">=",
                PathConstraint.getValue(pathConstraint));
    }

    private String getLessThanConstraint(TreeNode treeNode, PathConstraint pathConstraint){
        return join(treeNode.getParent().getVariableName() + "." +
                treeNode.getGraphicalName(),
                "<",
                PathConstraint.getValue(pathConstraint));
    }

    private String getLessThanEqualsConstraint(TreeNode treeNode, PathConstraint pathConstraint){
        return join(treeNode.getParent().getVariableName() + "." +
                treeNode.getGraphicalName(),
                "<=",
                PathConstraint.getValue(pathConstraint));
    }

    private String getRangeConstraint(TreeNode treeNode,
                                      PathConstraint pathConstraint,
                                      ConstraintType constraintType){
        if(!treeNode.getName().equals("chromosomeLocation")){
            return "<" + constraintType.name() + " UNSUPPORTED ON " + treeNode.getName() + ">";
        }
        List<String> ranges = new ArrayList<>(PathConstraint.getValues(pathConstraint));
        PathConstraintRange pcr = new PathConstraintRange(pathConstraint.getPath(),
        pathConstraint.getOp(),
        ranges);
        String constraintString = "";
        for (String range: pcr.getValues()) {
            GenomicInterval interval = new GenomicInterval(range);
            String chromosomePrimaryId = interval.getChr();
            int start = interval.getStart();
            int end = interval.getEnd();
            if (!constraintString.equals("")) {
                constraintString += " OR ";
            }
            if (start <= end) {
                constraintString += "(";
                if (constraintType == ConstraintType.OVERLAPS) {
                        constraintString += treeNode.getVariableName() + ".start <= " + end +
                                    " AND " +
                                    treeNode.getVariableName() + ".end >= " + start;

                }
                else if (constraintType == ConstraintType.CONTAINS) {
                    constraintString += "(" +
                                    treeNode.getVariableName() + ".start <= " + start +
                                    " AND " +
                                    treeNode.getVariableName() + ".end >= " + end;
                }
                else if (constraintType == ConstraintType.WITHIN) {
                    constraintString += "(" +
                                    treeNode.getVariableName() + ".start >= " + start +
                                    " AND " +
                                    treeNode.getVariableName() + ".end <= " + end;
                }
                constraintString += " AND " +
                                "(" + treeNode.getVariableName() + ")--(:Chromosome {primaryIdentifier:" +
                                Helper.quoted(chromosomePrimaryId) + "})" +
                                ")";
            }
            else {
                return "<INVALID RANGE ENTERED>";
            }
        }
        return constraintString;
    }

    private String getContainsConstraint(TreeNode treeNode, PathConstraint pathConstraint){
        // If CONTAINS is matching Strings
        if (!(PathConstraint.getValue(pathConstraint) == null)) {
            return join(treeNode.getParent().getVariableName() + "." +
            treeNode.getGraphicalName(),
            "CONTAINS",
            Helper.quoted(PathConstraint.getValue(pathConstraint)));
        }
        else {
            // If CONTAINS is matching Intervals
            return getRangeConstraint(treeNode, pathConstraint, ConstraintType.CONTAINS);
        }
    }

    private String getOverlapsConstraint(TreeNode treeNode, PathConstraint pathConstraint){
        return getRangeConstraint(treeNode, pathConstraint, ConstraintType.OVERLAPS);
    }

    private String getWithinConstraint(TreeNode treeNode, PathConstraint pathConstraint){
        return getRangeConstraint(treeNode, pathConstraint, ConstraintType.WITHIN);
    }

    public Constraint(PathConstraint pathConstraint, PathTree pathTree){
        this.type = ConstraintConverter.getConstraintType(pathConstraint);
        TreeNode treeNode = pathTree.getTreeNode(pathConstraint.getPath());

        switch (type) {

            case AND:
                constraint = getAndConstraint(treeNode, pathConstraint);
                break;

            case NAND:
                constraint = negation(getAndConstraint(treeNode, pathConstraint));
                break;

            case OR:
                constraint = getOrConstraint(treeNode, pathConstraint);
                break;

            case NOR:
                constraint = negation(getOrConstraint(treeNode, pathConstraint));
                break;

            case CONTAINS:
                constraint = getContainsConstraint(treeNode, pathConstraint);
                break;

            case DOES_NOT_CONTAIN:
                constraint = negation(getContainsConstraint(treeNode, pathConstraint));
                break;

            // Exact Match is same as Equating Two Strings
            case EXACT_MATCH:
            case EQUALS:
                constraint = getEqualsConstraint(treeNode, pathConstraint);
                break;

            case STRICT_NOT_EQUALS:
            case NOT_EQUALS:
                constraint = negation(getEqualsConstraint(treeNode, pathConstraint));
                break;

            case GREATER_THAN:
                constraint = getGreaterThanConstraint(treeNode, pathConstraint);
                break;

            case GREATER_THAN_EQUALS:
                constraint = getGreaterThanEqualsConstraint(treeNode, pathConstraint);
                break;

            case LESS_THAN:
                constraint = getLessThanConstraint(treeNode, pathConstraint);
                break;

            case LESS_THAN_EQUALS:
                constraint = getLessThanEqualsConstraint(treeNode, pathConstraint);
                break;

            // IS EMPTY is synonymous to IS NULL
            case IS_EMPTY:
            case IS_NULL:
                constraint = getIsNullConstraint(treeNode);
                break;

            // IS NOT EMPTY is synonymous to IS NOT NULL
            case IS_NOT_EMPTY:
            case IS_NOT_NULL:
                constraint = negation(getIsNullConstraint(treeNode));
                break;

            case ONE_OF:
                constraint = getOneOfConstraint(treeNode, pathConstraint);
                break;

            case NONE_OF:
                constraint = negation(getOneOfConstraint(treeNode, pathConstraint));
                break;

            case EXISTS:
                constraint = getExistsConstraint(treeNode);
                break;

            case DOES_NOT_EXIST:
                constraint = negation(getExistsConstraint(treeNode));
                break;

            case LOOKUP:
                constraint = getLookupConstraint(treeNode, pathConstraint);
                break;

            // Matches properties with Regular Expression
            case MATCHES:
                constraint = getMatchesConstraint(treeNode, pathConstraint);
                break;

            case DOES_NOT_MATCH:
                constraint = negation(getMatchesConstraint(treeNode, pathConstraint));
                break;

            case IN:
                constraint = getInConstraint(treeNode, pathConstraint);
                break;

            case NOT_IN:
                constraint = negation(getInConstraint(treeNode, pathConstraint));
                break;

            case WITHIN:
                constraint = getWithinConstraint(treeNode, pathConstraint);
                break;

            case OUTSIDE:
                constraint = negation(getWithinConstraint(treeNode, pathConstraint));

            case OVERLAPS:
                constraint = getOverlapsConstraint(treeNode, pathConstraint);
                break;

            case DOES_NOT_OVERLAP:
                constraint = negation(getOverlapsConstraint(treeNode, pathConstraint));
                break;

            case HAS:

            case DOES_NOT_HAVE:

            case ISA:
                constraint = getIsAConstraint(treeNode, pathConstraint);
                break;

            case ISNT:
                constraint = negation(getIsAConstraint(treeNode, pathConstraint));
                break;

            case UNSUPPORTED_CONSTRAINT:
                this.constraint = "<UNSUPPORTED CONSTRAINT>";
                break;
        }
    }

    private String getIsAConstraint(TreeNode treeNode, PathConstraint pathConstraint) {
        return "ANY(x IN labels(" +
                treeNode.getVariableName() +
                ") WHERE x IN " +
                Helper.quoted(PathConstraint.getValues(pathConstraint)) +
                ")";
    }

    /**
     * Converts a cypher constraint to its string representation.
     *
     * @return the string representation of the constraint
     */
    @Override
    public String toString(){
        return constraint;
    }

}
