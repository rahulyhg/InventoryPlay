package com.dell.inventoryplay.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 11/18/2017.
 * ChartTableResponse
 */

public class ChartTableResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("chartList")
    private ArrayList<Chart> chartList;
    @SerializedName("isSuccess")
    private Boolean isSuccess;
    @SerializedName("message")
    private String Message;
    public ChartTableResponse() {
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public ArrayList<Chart> getChartList() {
        return chartList;
    }

    public class Chart {
        private static final long serialVersionUID = 2L;
        @SerializedName("name")
        private String name;
        private int position;
        private int isMaximize;
        public int isMaximize() {
            return isMaximize;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setMaximize(int maximize) {
            this.isMaximize = maximize;
        }



        @SerializedName("axis1")
        private String[] axis1;
        @SerializedName("axis2")
        private String[] axis2;
        @SerializedName("values")
        private ArrayList<String[]> values;
        private boolean[] showList = null;
        private boolean[] maximize = null;


        public String getName() {
            return name;
        }

        public String[] getAxis1() {


            return axis1;
        }

        public void setMaximize(boolean[] maximize) {
            this.maximize = maximize;
        }

        public boolean[] getMaximize() {
            return maximize;
        }

        public void setShowList(boolean[] showList) {
            this.showList = showList;
        }

        public boolean[] getShowList() {
            return showList;
        }

        public String[] getAxis2() {
            if (getShowList() == null) {
                boolean showList[] = new boolean[axis2.length];
                boolean maximize[] = new boolean[axis2.length];
                for (int i = 0; i < axis2.length; i++) {
                    showList[i] = true;
                    maximize[i] = i == 0 ? true : false;
                }
                setShowList(showList);
                setMaximize(maximize);
            }
            return axis2;
        }

        public ArrayList<String[]> getValues() {
            return values;
        }
    }


}