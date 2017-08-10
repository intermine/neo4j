package org.intermine.neo4j.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Yash Sharma
 */
@XmlRootElement
public class ErrorMessage {
    private List<String> results;

    private String executionTime;

    private boolean wasSuccessful;

    private String error;

    private int statusCode;

    public ErrorMessage() {
    }

    public ErrorMessage(String error, int statusCode) {
        this.error = error;
        this.statusCode = statusCode;
        wasSuccessful = false;

        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        executionTime = dateFormat.format(new Date());
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public boolean isWasSuccessful() {
        return wasSuccessful;
    }

    public void setWasSuccessful(boolean wasSuccessful) {
        this.wasSuccessful = wasSuccessful;
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
}
