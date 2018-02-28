package com.dell.inventoryplay.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 11/18/2017.
 * CheckPointResponse
 */

public class CheckPointResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("titleList")
    private ArrayList<TitleList> titleList;
    @SerializedName("isSuccess")
    private Boolean isSuccess;
    @SerializedName("message")
    private String Message;

    public ArrayList<TitleList> getTitleList() {
        return titleList;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public CheckPointResponse() {
    }

    public class TitleList {

        private static final long serialVersionUID = 2L;
        @SerializedName("id")
        private String id;
        @SerializedName("title")
        private String title;

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }

}