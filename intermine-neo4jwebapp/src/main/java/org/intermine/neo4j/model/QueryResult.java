package org.intermine.neo4j.model;

import javax.activation.UnsupportedDataTypeException;
import javax.json.*;
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

    private List<List<Object>> results;

    public QueryResult() {
        columnHeaders = new ArrayList<>();
        results = new ArrayList<>();
    }

    public QueryResult(List<String> columnHeaders, List<List<Object>> results) {
        this.columnHeaders = columnHeaders;
        this.results = results;
    }

    public List<String> getColumnHeaders() {
        return columnHeaders;
    }

    public void setColumnHeaders(List<String> columnHeaders) {
        this.columnHeaders = columnHeaders;
    }

    public List<List<Object>> getResults() {
        return results;
    }

    public void setResults(List<List<Object>> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "QueryResult{" +
                "columnHeaders=" + columnHeaders +
                ", results=" + results +
                '}';
    }

    public void addHeader(String header) {
        this.columnHeaders.add(header);
    }

    public void addResultRow(List<Object> row) {
        this.results.add(row);
    }

    public JsonArray getResultsAsJsonArray() throws UnsupportedDataTypeException {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (List<Object> row : results) {
            JsonArrayBuilder rowJsonArrayBuilder = Json.createArrayBuilder();
            for (Object value : row) {
                if(value == null) {
                    rowJsonArrayBuilder.add(JsonObject.NULL);
                }
                else {
                    String str = value.getClass().getName();
                    if (str.equals("java.lang.Integer")) {
                        rowJsonArrayBuilder.add(Integer.parseInt(value.toString()));
                    }
                    else if(str.equals("java.lang.Double")) {
                        rowJsonArrayBuilder.add(Double.parseDouble(value.toString()));
                    }
                    else if(str.equals("java.lang.Boolean")) {
                        rowJsonArrayBuilder.add(Boolean.parseBoolean(value.toString()));
                    }
                    else if(str.equals("java.lang.Float")) {
                        rowJsonArrayBuilder.add(Float.parseFloat(value.toString()));
                    }
                    else if(str.equals("java.lang.String")){
                        if (value.toString().equals("null")) {
                            rowJsonArrayBuilder.add(JsonObject.NULL);
                        }
                        else {
                            rowJsonArrayBuilder.add(value.toString());
                        }
                    }
                    else {
                        throw new UnsupportedDataTypeException("Data Type " +
                                value.getClass().getName() +
                                " not supported in QueryResult.getResultsAsJsonArray().");
                    }
                }
            }
            jsonArrayBuilder.add(rowJsonArrayBuilder.build());
        }
        return jsonArrayBuilder.build();
    }

    public JsonArray getHeadersAsJsonArray() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (String header : columnHeaders) {
            jsonArrayBuilder.add(header);
        }
        return jsonArrayBuilder.build();
    }

    public JsonArray toJSON() throws UnsupportedDataTypeException {
        JsonArray value = Json.createArrayBuilder()
                                        .add(Json.createObjectBuilder()
                                                .add("results", getResultsAsJsonArray()))
                                        .add(Json.createObjectBuilder()
                                                .add("columnHeaders", getHeadersAsJsonArray()))
                                        .build();
        return value;
    }
}
