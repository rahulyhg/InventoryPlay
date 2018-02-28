package com.dell.inventoryplay.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 11/18/2017.
 * AsnResponse
 */

public class LoginResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("isValidUser")
    private Boolean isValidUser;
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
    public LoginResponse() {
    }

    public Boolean getValidUser() {
        return isValidUser;
    }
}