package org.intermine.neo4j.model;

/**
 * @author Yash Sharma
 */
public enum ColumnHeadersType {
    NONE, PATH, FRIENDLY;
    public ColumnHeadersType fromString(String param) {
        String toUpper = param.toUpperCase();
        try {
            return valueOf(toUpper);
        } catch (Exception e) {
            return null;
        }
    }
}
