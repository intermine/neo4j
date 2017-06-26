package org.intermine.neo4j.cypher;

/**
 * Describes a constraint type in Cypher Query.
 *
 * @author Yash Sharma
 */
public enum ConstraintType {
    AND,
    CONTAINS,
    DOES_NOT_CONTAIN,
    DOES_NOT_EXIST,
    DOES_NOT_HAVE,
    DOES_NOT_MATCH,
    DOES_NOT_OVERLAP,
    EQUALS,
    EXACT_MATCH,
    EXISTS,
    GREATER_THAN,
    GREATER_THAN_EQUALS,
    HAS,
    IN,
    IS_EMPTY,
    IS_NOT_EMPTY,
    IS_NOT_NULL,
    IS_NULL,
    ISA,
    ISNT,
    LESS_THAN,
    LESS_THAN_EQUALS,
    LOOKUP,
    MATCHES,
    NAND,
    NONE_OF,
    NOR,
    NOT_EQUALS,
    NOT_IN,
    ONE_OF,
    OR,
    OUTSIDE,
    OVERLAPS,
    STRICT_NOT_EQUALS,
    WITHIN,
    LIKE,
    NOT_LIKE,
    SOMETHING_NEW   // TO DO : Use a better default than this
}
