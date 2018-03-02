package com.dell.inventoryplay.request;

import org.json.JSONObject;


public class HttpRequestObject {
    private static HttpRequestObject instance = null;

    private HttpRequestObject() {
    }

    public static HttpRequestObject getInstance() {
        if (instance == null)
            instance = new HttpRequestObject();
        return instance;
    }

    public JSONObject getRequestBody(Object... params) throws Exception {

        String inputData = params[1].toString();
        return new JSONObject(inputData);
    }

}
