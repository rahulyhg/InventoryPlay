package com.dell.inventoryplay.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sasikanta_Sahoo on 11/18/2017.
 * ChartTableResponse
 */

public class HealthCheckOldResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("graph1")
    private Chart1 chart1;
    @SerializedName("graph2")
    private Chart2 chart2;
    @SerializedName("graph3")
    private Chart3 chart3;
    @SerializedName("graph4")
    private Chart4 chart4;

    public HealthCheckOldResponse() {
    }

    public Chart1 getChart1() {
        return chart1;
    }

    public Chart2 getChart2() {
        return chart2;
    }

    public Chart3 getChart3() {
        return chart3;
    }

    public Chart4 getChart4() {
        return chart4;
    }

    public class Chart1 {
        private static final long serialVersionUID = 2L;
        @SerializedName("apcc")
        private Chart1Reg apcc;
        @SerializedName("brh")
        private Chart1Reg brh;
        @SerializedName("dao")
        private Chart1Reg dao;
        @SerializedName("ccc")
        private Chart1Reg ccc;
        @SerializedName("emea")
        private Chart1Reg emea;
        @SerializedName("dgpc")
        private Chart1Reg dgpc;

        public Chart1Reg getApcc() {
            return apcc;
        }

        public Chart1Reg getBrh() {
            return brh;
        }

        public Chart1Reg getDao() {
            return dao;
        }

        public Chart1Reg getCcc() {
            return ccc;
        }

        public Chart1Reg getEmea() {
            return emea;
        }

        public Chart1Reg getDgpc() {
            return dgpc;
        }
    }

    public class Chart2 {
        private static final long serialVersionUID = 3L;
        @SerializedName("apcc")
        private Chart2Reg apcc;
        @SerializedName("brh")
        private Chart2Reg brh;
        @SerializedName("dao")
        private Chart2Reg dao;
        @SerializedName("ccc")
        private Chart2Reg ccc;
        @SerializedName("emea")
        private Chart2Reg emea;
        @SerializedName("dgpc")
        private Chart2Reg dgpc;

        public Chart2Reg getApcc() {
            return apcc;
        }

        public Chart2Reg getBrh() {
            return brh;
        }

        public Chart2Reg getDao() {
            return dao;
        }

        public Chart2Reg getCcc() {
            return ccc;
        }

        public Chart2Reg getEmea() {
            return emea;
        }

        public Chart2Reg getDgpc() {
            return dgpc;
        }
    }

    public class Chart3 {
        private static final long serialVersionUID = 4L;
        @SerializedName("apcc")
        private Chart3Reg apcc;
        @SerializedName("brh")
        private Chart3Reg brh;
        @SerializedName("dao")
        private Chart3Reg dao;
        @SerializedName("ccc")
        private Chart3Reg ccc;
        @SerializedName("emea")
        private Chart3Reg emea;
        @SerializedName("dgpc")
        private Chart3Reg dgpc;

        public Chart3Reg getApcc() {
            return apcc;
        }

        public Chart3Reg getBrh() {
            return brh;
        }

        public Chart3Reg getDao() {
            return dao;
        }

        public Chart3Reg getCcc() {
            return ccc;
        }

        public Chart3Reg getEmea() {
            return emea;
        }

        public Chart3Reg getDgpc() {
            return dgpc;
        }
    }

    public class Chart4 {
        private static final long serialVersionUID = 5L;
        @SerializedName("apcc")
        private Chart4Reg apcc;
        @SerializedName("brh")
        private Chart4Reg brh;
        @SerializedName("dao")
        private Chart4Reg dao;
        @SerializedName("ccc")
        private Chart4Reg ccc;
        @SerializedName("emea")
        private Chart4Reg emea;
        @SerializedName("dgpc")
        private Chart4Reg dgpc;

        public Chart4Reg getApcc() {
            return apcc;
        }

        public Chart4Reg getBrh() {
            return brh;
        }

        public Chart4Reg getDao() {
            return dao;
        }

        public Chart4Reg getCcc() {
            return ccc;
        }

        public Chart4Reg getEmea() {
            return emea;
        }

        public Chart4Reg getDgpc() {
            return dgpc;
        }
    }

    public class Chart1Reg {
        private static final long serialVersionUID = 6L;
        @SerializedName("asnRecv")
        private String asnRecv;
        @SerializedName("valAck")
        private String valAck;
        @SerializedName("snRecv")
        private String snRecv;
        @SerializedName("snAck")
        private String snAck;

        public String getAsnRecv() {
            return asnRecv;
        }

        public String getValAck() {
            return valAck;
        }

        public String getSnRecv() {
            return snRecv;
        }

        public String getSnAck() {
            return snAck;
        }
    }

    public class Chart2Reg {
        private static final long serialVersionUID = 7L;
        @SerializedName("k943")
        private Chart2RegSub k943;
        @SerializedName("k858")
        private Chart2RegSub k858;

        public Chart2RegSub getK943() {
            return k943;
        }

        public Chart2RegSub getK858() {
            return k858;
        }
    }

    public class Chart2RegSub {
        private static final long serialVersionUID = 8L;
        @SerializedName("recv")
        private String recv;
        @SerializedName("sent")
        private String sent;

        public String getRecv() {
            return recv;
        }

        public String getSent() {
            return sent;
        }
    }

    public class Chart3Reg {
        private static final long serialVersionUID = 9L;
        @SerializedName("relif")
        private String relif;
        @SerializedName("stage")
        private String stage;
        @SerializedName("ack")
        private String ack;
        @SerializedName("summarized")
        private String summarized;
        @SerializedName("validation")
        private String validation;
        @SerializedName("preStage")
        private String preStage;

        public String getRelif() {
            return relif;
        }

        public String getStage() {
            return stage;
        }

        public String getAck() {
            return ack;
        }

        public String getSummarized() {
            return summarized;
        }

        public String getValidation() {
            return validation;
        }

        public String getPreStage() {
            return preStage;
        }
    }

    public class Chart4Reg {
        private static final long serialVersionUID = 10L;
        @SerializedName("started")
        private String started;
        @SerializedName("ended")
        private String ended;

        public String getStarted() {
            return started;
        }

        public String getEnded() {
            return ended;
        }
    }
}