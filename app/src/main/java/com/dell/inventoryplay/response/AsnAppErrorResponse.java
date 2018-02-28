package com.dell.inventoryplay.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 11/18/2017.
 * AsnResponse
 */

public class AsnAppErrorResponse implements Serializable {
    private static final long serialVersionUID = 1L;




    @SerializedName("RESULT")
    private ArrayList<Records> records;


    public AsnAppErrorResponse() {
    }

    public ArrayList<Records> getRecords() {
        return records;
    }

    public class Records {
        private static final long serialVersionUID = 2L;
        @SerializedName("SVC_TAG")
        private String svcTag;
        @SerializedName("ERROR_MSG")
        private String errorMsg;
        @SerializedName("DATE_TIME")
        private String dateTime;
        @SerializedName("RECORD_STATUS")
        private String recordStatus;

        public String getSvcTag() {
            return svcTag;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public String getDateTime() {
            return dateTime;
        }

        public String getRecordStatus() {
            return recordStatus;
        }
    }

}