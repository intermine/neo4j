package org.intermine.neo4j.resource.bean;

import io.swagger.annotations.ApiParam;
import org.intermine.neo4j.model.ColumnHeadersType;
import org.intermine.neo4j.model.FormatType;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

/**
 * Stores all the parameters passed to query/results endpoint.
 *
 * @author Yash Sharma
 */
public class QueryResultBean {

    @ApiParam(value = "A definition of the query to execute in Path-Query XML format", required = false)
    @QueryParam("query")
    private String pathQuery;

    @ApiParam(value = "The version of the XML format used", required = false)
    @QueryParam("version")
    private int version;

    @ApiParam(value = "The index of the first result to return.", required = false)
    @QueryParam("start")
    private int start;

    @ApiParam(value = "The maximum size of the result set.", required = false)
    @QueryParam("size")
    @DefaultValue("10")
    private int size;

    @ApiParam(value = "Include column headers. Use friendly for human readable paths. (Only for flat-file formats)", required = false)
    @QueryParam("columnheaders")
    private ColumnHeadersType columnHeadersType;

    @ApiParam(value = "Output format", required = false)
    @QueryParam("format")
    private FormatType formatType;

    public String getPathQuery() {
        return pathQuery;
    }

    public void setPathQuery(String pathQuery) {
        this.pathQuery = pathQuery;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public ColumnHeadersType getColumnHeadersType() {
        return columnHeadersType;
    }

    public void setColumnHeadersType(ColumnHeadersType columnHeadersType) {
        this.columnHeadersType = columnHeadersType;
    }

    public FormatType getFormatType() {
        return formatType;
    }

    public void setFormatType(FormatType formatType) {
        this.formatType = formatType;
    }
}
