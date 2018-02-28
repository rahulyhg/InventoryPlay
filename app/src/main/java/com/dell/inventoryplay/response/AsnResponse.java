package com.dell.inventoryplay.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 11/18/2017.
 * AsnResponse
 */

public class AsnResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("HEADER_INFO")
    private ArrayList<HeaderInfo> headerInfo;
    @SerializedName("APP_INFO")
    private ArrayList<Records> records;

    public ArrayList<HeaderInfo> getHeaderInfo() {
        return headerInfo;
    }

    public AsnResponse() {
    }

    public ArrayList<Records> getRecords() {
        return records;
    }

    public class HeaderInfo implements Serializable {
        private static final long serialVersionUID = 2L;
        @SerializedName("ID")
        private String id;
        @SerializedName("KEY")
        private String key;
        @SerializedName("VALUE")
        private String value;

        public String getId() {
            return id;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    public class Records implements Serializable {
        private static final long serialVersionUID = 3L;
        @SerializedName("APP_NAME")
        private String appName;
        @SerializedName("MSG_TYPE")
        private String msgType;
        @SerializedName("DATE_TIME")
        private String dateTime;

        public String getAppName() {
            return appName;
        }

        public String getMsgType() {
            return msgType;
        }

        public String getDateTime() {
            return dateTime;
        }
    }

}