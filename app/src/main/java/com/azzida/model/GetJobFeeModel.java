package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetJobFeeModel {

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

        @SerializedName("Id")
        @Expose
        private Integer id;
        @SerializedName("JobSeekerFee")
        @Expose
        private String jobSeekerFee;
        @SerializedName("JobListerFee")
        @Expose
        private String jobListerFee;
        @SerializedName("CreateDate")
        @Expose
        private String createDate;
        @SerializedName("BackgroundCheck")
        @Expose
        private String backgroundCheck;
        @SerializedName("CancelJobFee")
        @Expose
        private String cancelJobFee;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getJobSeekerFee() {
            return jobSeekerFee;
        }

        public void setJobSeekerFee(String jobSeekerFee) {
            this.jobSeekerFee = jobSeekerFee;
        }

        public String getJobListerFee() {
            return jobListerFee;
        }

        public void setJobListerFee(String jobListerFee) {
            this.jobListerFee = jobListerFee;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getBackgroundCheck() {
            return backgroundCheck;
        }

        public void setBackgroundCheck(String backgroundCheck) {
            this.backgroundCheck = backgroundCheck;
        }

        public String getCancelJobFee() {
            return cancelJobFee;
        }

        public void setCancelJobFee(String cancelJobFee) {
            this.cancelJobFee = cancelJobFee;
        }
    }
}
