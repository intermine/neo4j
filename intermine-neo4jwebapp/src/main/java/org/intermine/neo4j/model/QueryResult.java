package org.intermine.neo4j.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yash Sharma
 */
@XmlRootElement
public class QueryResult {

    private List<String> columnHeaders;

    private List<List<String>> results;

    // Empty constructor. Its absence causes Internal Server Error.
    public QueryResult() {

    }

    public QueryResult(List<String> columnHeaders, List<List<String>> results) {
        this.columnHeaders = columnHeaders;
        this.results = results;
    }

    public List<String> getColumnHeaders() {
        return columnHeaders;
    }

    public void setColumnHeaders(List<String> columnHeaders) {
        this.columnHeaders = columnHeaders;
    }

    public List<List<String>> getResults() {
        return results;
    }

    public void setResults(List<List<String>> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "QueryResult{" +
                "columnHeaders=" + columnHeaders +
                ", results=" + results +
                '}';
    }
}
