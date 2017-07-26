package org.intermine.neo4j.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Yash Sharma
 */
@XmlRootElement
public class QueryResult {
    String result;

    public QueryResult(String result) {
        this.result = result;
    }
}
