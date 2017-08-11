package org.intermine.neo4j.resource.bean;

import io.swagger.annotations.ApiParam;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

/**
 * Stores all the parameters passed to query/cypher endpoint.
 *
 * @author Yash Sharma
 */
public class QueryCypherBean {
    @ApiParam(value = "A definition of the query to execute in Path-Query XML format", required = false, defaultValue = "<query model=\"genomic\" view=\"Gene.primaryIdentifier Gene.organism.commonName Gene.length Gene.organism.name\" constraintLogic=\"(A)\">\n" +
            "    <constraint path=\"Gene.primaryIdentifier\" value=\"fb\" op=\"CONTAINS\" code=\"A\"/>\n" +
            "</query>")
    @QueryParam("query")
    private String pathQuery;

    @ApiParam(value = "The index of the first result to return.", required = false)
    @QueryParam("start")
    private int start;

    @ApiParam(value = "The maximum size of the result set.", required = false)
    @QueryParam("size")
    @DefaultValue("10")
    private int size;

    public String getPathQuery() {
        return pathQuery;
    }

    public void setPathQuery(String pathQuery) {
        this.pathQuery = pathQuery;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
