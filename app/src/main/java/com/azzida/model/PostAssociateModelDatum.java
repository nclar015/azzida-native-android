package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostAssociateModelDatum {

    @SerializedName("postassociate")
    @Expose
    private String postassociate;
    @SerializedName("Amount")
    @Expose
    private Integer amount;
    @SerializedName("Id")
    @Expose
    private Integer id;

    public String getPostassociate() {
        return postassociate;
    }

    public void setPostassociate(String postassociate) {
        this.postassociate = postassociate;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}

