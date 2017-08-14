package org.intermine.neo4j.model;

/**
 * @author Yash Sharma
 */
public enum FormatType {
    TAB, JSON;

    public FormatType fromString(String param) {
        String toUpper = param.toUpperCase();
        try {
            return valueOf(toUpper);
        } catch (Exception e) {
            return null;
        }
    }
}
