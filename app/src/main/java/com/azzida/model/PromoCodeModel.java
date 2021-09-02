package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Logictrix on 07-Jan-21.
 */
public class PromoCodeModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<Datum> data = null;

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

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }



    public class Datum {

        @SerializedName("Id")
        @Expose
        private Integer id;
        @SerializedName("NamePromo")
        @Expose
        private String namePromo;
        @SerializedName("Code")
        @Expose
        private String code;
        @SerializedName("TypeDiscount")
        @Expose
        private String typeDiscount;
        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;
        @SerializedName("Amount")
        @Expose
        private String amount;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNamePromo() {
            return namePromo;
        }

        public void setNamePromo(String namePromo) {
            this.namePromo = namePromo;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTypeDiscount() {
            return typeDiscount;
        }

        public void setTypeDiscount(String typeDiscount) {
            this.typeDiscount = typeDiscount;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

    }

}
