package com.dell.inventoryplay.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 11/18/2017.
 * AsnResponse
 */

public class AsnAppDetailsResponse implements Serializable {
    private static final long serialVersionUID = 1L;


    @SerializedName("RESULT")
    private ArrayList<Records> records;


    public AsnAppDetailsResponse() {
    }

    public ArrayList<Records> getRecords() {
        return records;
    }

    public class Records {
        private static final long serialVersionUID = 2L;
        @SerializedName("SUB_TRANS_NAME")
        private String subTransName;
        @SerializedName("MSG_TYPE")
        private String msgType;
        @SerializedName("PROCESSING_TIME")
        private String processingDateTime;

        public String getSubTransName() {
            return subTransName;
        }

        public String getMsgType() {
            return msgType;
        }

        public String getProcessingDateTime() {
            return processingDateTime;
        }
    }

}