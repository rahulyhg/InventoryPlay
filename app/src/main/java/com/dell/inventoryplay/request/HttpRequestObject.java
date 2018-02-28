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
        int code = Integer.parseInt(params[0].toString());
        String inputData = params[1].toString();
        JSONObject jsonObj = new JSONObject(inputData);
      /*  jsonObj.put("API_CODE", code);
        jsonObj.put("DEVICE", "android");
        jsonObj.put("OS_VERSION", android.os.Build.VERSION.RELEASE);
        */return jsonObj;
    }

}
