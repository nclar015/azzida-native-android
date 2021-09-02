package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Logictrix on 22-Jan-21.
 */
public class GetCheckerReportModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("ReportStatus")
        @Expose
        private String reportStatus;
        @SerializedName("NationalStatus")
        @Expose
        private String nationalStatus;
        @SerializedName("GlobalStatus")
        @Expose
        private String globalStatus;
        @SerializedName("SexOffenderStatus")
        @Expose
        private String sexOffenderStatus;
        @SerializedName("SsnTraceStatus")
        @Expose
        private String ssnTraceStatus;
        @SerializedName("UserId")
        @Expose
        private Integer userId;

        public String getReportStatus() {
            return reportStatus;
        }

        public void setReportStatus(String reportStatus) {
            this.reportStatus = reportStatus;
        }

        public String getNationalStatus() {
            return nationalStatus;
        }

        public void setNationalStatus(String nationalStatus) {
            this.nationalStatus = nationalStatus;
        }

        public String getGlobalStatus() {
            return globalStatus;
        }

        public void setGlobalStatus(String globalStatus) {
            this.globalStatus = globalStatus;
        }

        public String getSexOffenderStatus() {
            return sexOffenderStatus;
        }

        public void setSexOffenderStatus(String sexOffenderStatus) {
            this.sexOffenderStatus = sexOffenderStatus;
        }

        public String getSsnTraceStatus() {
            return ssnTraceStatus;
        }

        public void setSsnTraceStatus(String ssnTraceStatus) {
            this.ssnTraceStatus = ssnTraceStatus;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

    }
}
