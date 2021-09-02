package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SaveCardModelData {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("usercards")
    @Expose
    private ArrayList<Usercard> usercards = null;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<Usercard> getUsercards() {
        return usercards;
    }

    public void setUsercards(ArrayList<Usercard> usercards) {
        this.usercards = usercards;
    }

}