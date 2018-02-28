package com.dell.inventoryplay.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 1/7/2018.
 * InquirySvcTagResponse
 */

public class InquirySvcTagResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("columns")
    private ArrayList<String> columnList;
    @SerializedName("rows")
    private ArrayList<ArrayList<String>> valueList;
    @SerializedName("isSuccess")
    private Boolean isSuccess;
    @SerializedName("message")
    private String Message;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public InquirySvcTagResponse() {
    }

    public ArrayList<String> getColumnList() {
        return columnList;
    }

    public ArrayList<ArrayList<String>> getValueList() {
        return valueList;
    }
}
