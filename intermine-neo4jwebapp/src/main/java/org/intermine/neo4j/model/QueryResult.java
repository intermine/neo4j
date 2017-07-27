package org.intermine.neo4j.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Yash Sharma
 */
@XmlRootElement
public class QueryResult {
    private String result;

    // Empty constructor. Its absence causes Internal Server Error.
    public QueryResult() {

    }

    public QueryResult(String result) {
        this.result = result;
    }

    // Only the fields having public getters & setters are serialized.

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
