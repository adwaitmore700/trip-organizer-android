package com.uncc.mad.triporganizer.models;

public class ServerResponse {
    private String ErrorCode;
    private String ErrorDescription;
    private Object Response;            //Can be anything that needs to be transferred to the activity

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorDescription() {
        return ErrorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        ErrorDescription = errorDescription;
    }

    public Object getResponse() {
        return Response;
    }

    public void setResponse(Object response) {
        Response = response;
    }
}
