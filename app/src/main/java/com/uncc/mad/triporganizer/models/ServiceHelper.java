package com.uncc.mad.triporganizer.models;

import android.content.Context;

public class ServiceHelper {
    private Context ActivityContext;
    private String ActionIdentifier;
    private String ErrorCode;
    private String ErrorDescription;
    private Object Response;            //Can be anything that needs to be transferred to the activity

    public Context getActivityContext() {
        return ActivityContext;
    }

    public void setActivityContext(Context activityContext) {
        ActivityContext = activityContext;
    }

    public String getActionIdentifier() {
        return ActionIdentifier;
    }

    public void setActionIdentifier(String actionIdentifier) {
        ActionIdentifier = actionIdentifier;
    }

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
