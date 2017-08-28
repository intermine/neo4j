package org.intermine.neo4j.model;

import javax.activation.UnsupportedDataTypeException;
import javax.json.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Yash Sharma
 */
@XmlRootElement
public class QueryResult {

    private String rootClass;

    private String modelName;

    private int start;

    private List<String> views;

    private List<String> columnHeaders;

    private List<List<Object>> results;

    private String executionTime;

    private boolean successful;

    private String error;

    private int statusCode;

    public QueryResult() {
    }

    public QueryResult(String rootClass, String modelName, int start,
                       List<String> views, List<String> columnHeaders,
                       List<List<Object>> results, String executionTime,
                       boolean successful, String error, int statusCode) {
        this.rootClass = rootClass;
        this.modelName = modelName;
        this.start = start;
        this.views = views;
        this.columnHeaders = columnHeaders;
        this.results = results;
        this.executionTime = executionTime;
        this.successful = successful;
        this.error = error;
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "QueryResult{" +
                "rootClass='" + rootClass + '\'' +
                ",\nmodelName='" + modelName + '\'' +
                ",\nstart=" + start +
                ",\nviews=" + views +
                ",\ncolumnHeaders=" + columnHeaders +
                ",\nresults=" + results +
                ",\nexecutionTime='" + executionTime + '\'' +
                ",\nsuccessful=" + successful +
                ",\nerror='" + error + '\'' +
                ",\nstatusCode=" + statusCode +
                '}';
    }

    public String getRootClass() {
        return rootClass;
    }

    public void setRootClass(String rootClass) {
        this.rootClass = rootClass;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public List<String> getViews() {
        return views;
    }

    public void setViews(List<String> views) {
        this.views = views;
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

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    public void addHeader(String header) {
        this.columnHeaders.add(header);
    }

    public void addResultRow(List<Object> row) {
        this.results.add(row);
    }

    /**
     * Get Results as a JSON Array
     * @return A JSONArrayBuilder containing Results
     * @throws UnsupportedDataTypeException
     */
    public JsonArrayBuilder getResultsAsJsonArray() throws UnsupportedDataTypeException {
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
        return jsonArrayBuilder;
    }

    /**
     * Converts a list of strings to JSON Array
     * @param list A list of strings
     * @return A JsonArrayBuilder containing the strings
     */
    public JsonArrayBuilder getListAsJsonArray(List<String> list) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (String string : list) {
            if (string == null) {
                jsonArrayBuilder.addNull();
            }
            else {
                jsonArrayBuilder.add(string);
            }
        }
        return jsonArrayBuilder;
    }

    private JsonObjectBuilder addStringToJsonObjectBuilder(JsonObjectBuilder jsonObjectBuilder, String key, String value) {
        if (value != null) {
            return jsonObjectBuilder.add(key, value);
        }
        return jsonObjectBuilder.add(key, JsonValue.NULL);
    }

    /**
     * Converts QueryResult object to its JSON Representation
     * @return A JsonObject representing the Query Result
     * @throws UnsupportedDataTypeException
     */
    public JsonObject toJSON() throws UnsupportedDataTypeException {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder = addStringToJsonObjectBuilder(jsonObjectBuilder, "rootClass", getRootClass());
        jsonObjectBuilder = addStringToJsonObjectBuilder(jsonObjectBuilder, "modelName", getModelName());
        jsonObjectBuilder = jsonObjectBuilder.add("start", getStart())
                                            .add("views", getListAsJsonArray(getViews()))
                                            .add("columnHeaders", getListAsJsonArray(getColumnHeaders()))
                                            .add("results", getResultsAsJsonArray());
        jsonObjectBuilder = addStringToJsonObjectBuilder(jsonObjectBuilder, "executionTime", getExecutionTime());
        jsonObjectBuilder = jsonObjectBuilder.add("wasSuccessful", isSuccessful());
        jsonObjectBuilder = addStringToJsonObjectBuilder(jsonObjectBuilder, "error", getError());
        jsonObjectBuilder = jsonObjectBuilder.add("statusCode", getStatusCode());

        return jsonObjectBuilder.build();
    }

    /**
     * Converts QueryResult object to its Tab Separated Values (TSV) Representation
     * @param columnHeadersType Type of Column Headers to be returned along with the result
     * @return A string which contains Tab Separated Results
     */
    public String toTSV(ColumnHeadersType columnHeadersType ) {
        StringBuilder stringBuilder = new StringBuilder();
        switch (columnHeadersType) {
            case PATH:
                for (String header : getViews()) {
                    stringBuilder.append(header).append("\t");
                }
                stringBuilder.append("\n");
                break;
            case FRIENDLY:
                for (String header : getColumnHeaders()) {
                    stringBuilder.append(header).append("\t");
                }
                stringBuilder.append("\n");
                break;
            case NONE:
            default:
                break;
        }
        for (List<Object> row : getResults()) {
            for (Object obj : row) {
                stringBuilder.append(obj.toString()).append("\t");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
